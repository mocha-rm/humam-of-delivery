package com.teamnine.humanofdelivery.dto;

import com.teamnine.humanofdelivery.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MenuResponseDto {

    private Long menuid;
    private String name;
    private Integer price;
    private MenuStatus menuStatus;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MenuResponseDto(Menu menu) {


        this.menuid = menu.getMenuId();
        this.name = menu.getMenuName();
        this.price = menu.getPrice();
        this.menuStatus = menu.getMenuStatus();
    }
}
