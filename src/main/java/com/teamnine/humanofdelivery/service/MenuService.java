package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.dto.MenuRequestDto;
import com.teamnine.humanofdelivery.dto.MenuResponseDto;
import com.teamnine.humanofdelivery.entity.Restaurant;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.entity.Menu;
import com.teamnine.humanofdelivery.entity.User;
import com.teamnine.humanofdelivery.repository.MenuRepository;
import com.teamnine.humanofdelivery.repository.RestaurantRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import com.teamnine.humanofdelivery.repository.UserRepository;
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
    private final UserRepository userRepository;

    /**
     * 메뉴 생성
     */
    public MenuResponseDto createMenu(MenuRequestDto requestDto, HttpSession session) {
        // 세션에서 로그인 사용자 확인
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // Store 엔티티 조회 (User가 소유한 Store)
        Store store = storeRepository.findByUserAndId(user, requestDto.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("해당 가게를 찾을 수 없습니다."));

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
        if (storeId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        // Menu 엔티티 조회
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다."));

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
        if (storeId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        // Menu 엔티티 조회
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다."));

        // 상태 변경
        menu.deleteMenu();

        return "삭제 되었습니다.";
    }
}
