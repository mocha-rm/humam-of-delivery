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
import com.teamnine.humanofdelivery.repository.MemberRepository;
import com.teamnine.humanofdelivery.repository.MenuRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
            //일반 사용자는 가게를 오픈할 수 없습니다.
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else if (storeRepository.findOpenStore(StoreStatus.OPEN, findMember.getUserId()) >= 3) {
            //폐업 상태가 아닌 가게를 3개까지 운영가능
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Store createdStore = storeRequestDto.toEntity(findMember);
        storeRepository.save(createdStore);
        return new StoreResponseDto(createdStore);
    }

    public List<StoreResponseDto> findAll(String name) {
        return storeRepository.findAllByStoreName(name, StoreStatus.SHUT).stream().map(StoreResponseDto::toDto).toList();
    }

    public StoreWithMenusResponseDto findStore(Long id) {
        Store findStore = storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Menu> activeMenus = menuRepository.findActiveMenusByStoreId(findStore.getId());

        return new StoreWithMenusResponseDto(findStore, activeMenus);
    }

    @Transactional
    public StoreResponseDto patchStoreStatus(Long id, StoreStatus storeStatus) {
        Member findMember = getAuthorizedMember();

        Store findStore = getStore(id);
        if (!Objects.equals(findMember.getUserId(), findStore.getMember().getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 가게만 폐업할 수 있습니다.");
        } else if (storeStatus.equals(findStore.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 " + storeStatus + "된 상태입니다.");
        }

        findStore.patchStatus(storeStatus);
        storeRepository.save(findStore);
        return new StoreResponseDto(findStore);
    }

    private Store getStore(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Member getAuthorizedMember() {
        Member findMember = memberRepository.findByEmailOrElseThrow(sessionUtils.getLoginUserEmail());
        sessionUtils.checkAuthorization(findMember);
        return findMember;
    }
}