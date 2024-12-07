package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.common.SessionNames;
import com.teamnine.humanofdelivery.dto.menu.MenuRequestDto;
import com.teamnine.humanofdelivery.dto.menu.MenuResponseDto;
import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.entity.Menu;
import com.teamnine.humanofdelivery.exception.menu.MenuErrorCode;
import com.teamnine.humanofdelivery.exception.menu.MenuException;
import com.teamnine.humanofdelivery.exception.store.StoreErrorCode;
import com.teamnine.humanofdelivery.exception.store.StoreException;
import com.teamnine.humanofdelivery.repository.MemberRepository;
import com.teamnine.humanofdelivery.repository.MenuRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import com.teamnine.humanofdelivery.status.MenuStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    /**
     * 로그인 세션에서 Member 정보를 가져옵니다.
     * @param session HttpSession
     * @return 로그인된 Member
     */
    private Member getMemberFromSession(HttpSession session) {
        String email = (String) session.getAttribute(SessionNames.USER_AUTH);
        if (email == null) {
            throw new IllegalStateException("로그인 정보가 유효하지 않습니다. 다시 로그인해주세요.");
        }
        Member member = memberRepository.findByEmailOrElseThrow(email);
        return member;
    }
    //메뉴 생성
    public MenuResponseDto createMenu(MenuRequestDto requestDto, HttpSession session) {

        // 세션에서 로그인 사용자 확인
        Member member = getMemberFromSession(session);
        Long memberId = member.getUserId();


        // Store 엔티티 조회 (Member가 소유한 Store)
        Store store = storeRepository.findByMember_IdAndId(memberId, requestDto.getStoreId())
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        // Menu 객체 생성
        Menu menu = Menu.builder()
                .menuName(requestDto.getMenuName())
                .price(requestDto.getPrice())
                .menuStatus(MenuStatus.ACTIVE)
                .store(store)
                .build();

        // DB에 저장
        Menu savedMenu = menuRepository.save(menu);

        // MenuResponseDto로 변환 후 반환
        return new MenuResponseDto(savedMenu);
    }


    public MenuResponseDto updateMenu(Long menuId, MenuRequestDto requestDto, HttpSession session) {

        // 세션에서 로그인 사용자 확인
        Long storeId = (Long) session.getAttribute("storeId");

        // Menu 엔티티 조회
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));

        // 엔티티 메서드를 통해 업데이트
        menu.updateMenu(requestDto.getMenuName(), requestDto.getPrice());

        // 변경된 엔티티로 ResponseDto 생성
        return new MenuResponseDto(menu);
    }

    /**
     * 메뉴 삭제 (status 변경)
     */
    public String deleteMenu(Long menuId, HttpSession session) {

        // 세션에서 로그인 사용자 확인
        Long storeId = (Long) session.getAttribute("storeId");

        // Menu 엔티티 조회
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException(MenuErrorCode.MENU_NOT_FOUND));

        // 상태 변경
        menu.deleteMenu();

        return "삭제 되었습니다.";
    }
}
