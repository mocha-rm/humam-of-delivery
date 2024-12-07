package com.teamnine.humanofdelivery.dto.menu;


import com.teamnine.humanofdelivery.entity.Menu;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;




@Getter
@NoArgsConstructor
public class MenuRequestDto {

    @NotNull
    private String menuName;

    @NotNull
    private Long storeId;

    @NotNull
    @Positive
    private Integer price;


    public Menu toEntity() {
        return Menu.builder()
                .menuName(this.menuName)
                .price(this.price)
                .build();

    }
}
