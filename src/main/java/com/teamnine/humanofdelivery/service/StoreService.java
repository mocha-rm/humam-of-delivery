package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.status.StoreStatus;
import com.teamnine.humanofdelivery.config.session.SessionUtils;
import com.teamnine.humanofdelivery.dto.store.StoreRequestDto;
import com.teamnine.humanofdelivery.dto.store.StoreResponseDto;
import com.teamnine.humanofdelivery.dto.store.StoreWithMenusResponseDto;
import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.entity.Menu;
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

    /**
     * 스토어 생성 로직
     *
     * @param storeRequestDto (이름, 상태, 최저주문금액, 오픈시간, 마감시간)
     * @return 스토어 정보 (id, 이름, 상태, 최저주문금액, 오픈시간, 마감시간, 생성일, 수정일)
     */
    @Transactional
    public StoreResponseDto create(StoreRequestDto storeRequestDto) {
        Member findMember = getAuthorizedMember();

        if (storeRepository.findOpenStore(StoreStatus.OPEN, findMember.getUserId()) >= 3) {
            throw new StoreException(StoreErrorCode.STORE_ERROR_OWNER_01);
        }

        Store createdStore = storeRequestDto.toEntity(findMember);
        storeRepository.save(createdStore);
        return new StoreResponseDto(createdStore);
    }

    /**
     * 모든 스토어 조회
     *
     * @param name (검색할 이름)
     * @return 검색한 이름이 포함된 모든 스토어 목록
     */
    public List<StoreResponseDto> findAll(String name) {
        return storeRepository.findAllByStoreName(name, StoreStatus.SHUT).stream().map(StoreResponseDto::toDto).toList();
    }

    /**
     * 특정 스토어 조회
     *
     * @param id (스토어 id)
     * @return (스토어 정보, 메뉴 목록)
     */
    public StoreWithMenusResponseDto findStore(Long id) {
        Store findStore = getStore(id);
        List<Menu> activeMenus = menuRepository.findActiveMenusByStoreId(findStore.getId());

        return new StoreWithMenusResponseDto(findStore, activeMenus);
    }

    /**
     * 스토어 상태 변경 (삭제 시 DB 상태 변경)
     *
     * @param id          (스토어 id)
     * @param storeStatus (OPEN, SHUT)
     * @return 스토어 정보 (id, 이름, 상태, 최저주문금액, 오픈시간, 마감시간, 생성일, 수정일)
     */
    @Transactional
    public StoreResponseDto patchStoreStatus(Long id, StoreStatus storeStatus) {
        Member findMember = getAuthorizedMember();

        Store findStore = getStore(id);
        if (!Objects.equals(findMember.getUserId(), findStore.getMember().getUserId())) {
            throw new StoreException(StoreErrorCode.STORE_ERROR_AUTHORIZATION);
        } else if (storeStatus.equals(findStore.getStatus())) {
            throw new StoreException(StoreErrorCode.STORE_ERROR_OWNER_02);
        }

        findStore.patchStatus(storeStatus);
        storeRepository.save(findStore);
        return new StoreResponseDto(findStore);
    }

    // id로 스토어 찾아서 반환
    private Store getStore(Long id) throws StoreException {
        return storeRepository.findById(id).orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
    }

    // 유저 권한 확인
    private Member getAuthorizedMember() {
        Member findMember = memberRepository.findByEmailOrElseThrow(sessionUtils.getLoginUserEmail());
        sessionUtils.checkAuthorization(findMember);
        return findMember;
    }
}