package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.StoreStatus;
import com.teamnine.humanofdelivery.config.session.SessionUtils;
import com.teamnine.humanofdelivery.dto.StoreRequestDto;
import com.teamnine.humanofdelivery.dto.StoreResponseDto;
import com.teamnine.humanofdelivery.dto.StoreWithMenusResponseDto;
import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.entity.Menu;
import com.teamnine.humanofdelivery.enums.UserRole;
import com.teamnine.humanofdelivery.exception.store.StoreErrorCode;
import com.teamnine.humanofdelivery.exception.store.StoreException;
import com.teamnine.humanofdelivery.repository.MemberRepository;
import com.teamnine.humanofdelivery.repository.MenuRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class StoreService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final SessionUtils sessionUtils;

    @Transactional
    public StoreResponseDto create(StoreRequestDto storeRequestDto) {
        Member findMember = getAuthorizedMember();

        if (findMember.getRole() != UserRole.OWNER) {
            throw new StoreException(StoreErrorCode.STORE_ERROR_AUTHORIZATION_01);
        } else if (storeRepository.findOpenStore(StoreStatus.OPEN, findMember.getUserId()) >= 3) {
            throw new StoreException(StoreErrorCode.STORE_ERROR_OWNER_01);
        }

        Store createdStore = storeRequestDto.toEntity(findMember);
        storeRepository.save(createdStore);
        return new StoreResponseDto(createdStore);
    }

    public List<StoreResponseDto> findAll(String name) {
        return storeRepository.findAllByStoreName(name, StoreStatus.SHUT).stream().map(StoreResponseDto::toDto).toList();
    }

    public StoreWithMenusResponseDto findStore(Long id) throws StoreException {
        Store findStore = getStore(id);
        List<Menu> activeMenus = menuRepository.findActiveMenusByStoreId(findStore.getId());

        return new StoreWithMenusResponseDto(findStore, activeMenus);
    }

    @Transactional
    public StoreResponseDto patchStoreStatus(Long id, StoreStatus storeStatus) {
        Member findMember = getAuthorizedMember();

        Store findStore = getStore(id);
        if (!Objects.equals(findMember.getUserId(), findStore.getMember().getUserId())) {
            throw new StoreException(StoreErrorCode.STORE_ERROR_AUTHORIZATION_02);
        } else if (storeStatus.equals(findStore.getStatus())) {
            throw new StoreException(StoreErrorCode.STORE_ERROR_OWNER_02);
        }

        findStore.patchStatus(storeStatus);
        storeRepository.save(findStore);
        return new StoreResponseDto(findStore);
    }

    private Store getStore(Long id) throws StoreException {
        return storeRepository.findById(id).orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
    }

    private Member getAuthorizedMember() {
        Member findMember = memberRepository.findByEmailOrElseThrow(sessionUtils.getLoginUserEmail());
        sessionUtils.checkAuthorization(findMember);
        return findMember;
    }
}