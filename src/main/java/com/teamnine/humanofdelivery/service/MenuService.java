package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.dto.MenuRequestDto;
import com.teamnine.humanofdelivery.dto.MenuResponseDto;
import com.teamnine.humanofdelivery.entity.Restaurant;
import com.teamnine.humanofdelivery.entity.base.Menu;
import com.teamnine.humanofdelivery.repository.MenuRepository;
import com.teamnine.humanofdelivery.repository.RestaurantRepository;
import com.teamnine.humanofdelivery.status.MenuStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * 메뉴 생성
     */
    public MenuResponseDto createMenu(MenuRequestDto requestDto) {
        // StoreId를 통해 Restaurant 엔티티 조회
        Restaurant restaurant = restaurantRepository.findById(requestDto.restaurantId())
                .orElseThrow(() -> new EntityNotFoundException("해당 가게를 찾을 수 없습니다."));

        // Menu 객체 생성
        Menu menu = Menu.builder()
                .menuName(requestDto.getMenuName())
                .price(requestDto.getPrice())
                .menuStatus(MenuStatus.ACTIVE)
                .restaurant(restaurant)
                .build();

        // DB에 저장
        Menu savedMenu = menuRepository.save(menu);

        // MenuResponseDto로 변환 후 반환
        return new MenuResponseDto(savedMenu);
    }

    public MenuResponseDto updateMenu(Long menuId, MenuRequestDto requestDto) {
        // Menu 엔티티 조회
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
    public String deleteMenu(Long menuId) {
        // Menu 엔티티 조회
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다."));

        // 상태 변경
        menu.deleteMenu();

        return "삭제 되었습니다.";
    }
}
