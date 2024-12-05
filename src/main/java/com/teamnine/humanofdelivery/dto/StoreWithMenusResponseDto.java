package com.teamnine.humanofdelivery.dto;

import com.teamnine.humanofdelivery.StoreStatus;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.entity.base.Menu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class StoreWithMenusResponseDto {
    private final Long id;
    private final String name;
    private final StoreStatus status;
    private final Integer minCost;
    private final LocalTime openAt;
    private final LocalTime closeAt;
    private final List<MenuResponseDto> menus;

    public StoreWithMenusResponseDto(Store store, List<Menu> menus) {
        this.id = store.getId();
        this.name = store.getName();
        this.status = store.getStatus();
        this.minCost = store.getMinCost();
        this.openAt = store.getOpenAt();
        this.closeAt = store.getCloseAt();
        this.menus = menus.stream()
                .map(MenuResponseDto::new)
                .toList();
    }
}
