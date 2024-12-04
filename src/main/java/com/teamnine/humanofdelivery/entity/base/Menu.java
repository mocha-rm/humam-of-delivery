package com.teamnine.humanofdelivery.entity.base;


import com.teamnine.humanofdelivery.status.MenuStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.engine.spi.Status;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn Column(name = "restaurantId", nullable = false);
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn Column(name = "orderId", nullable = false);
    private Order order;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "menuStatus", nullable = false)
    private MenuStatus menuStatus;


    @Builder
    public Menu(Long menuId, Restaurant rstaurant, String menuName, Integer price, MenuStatus menuStatus) {
        this.menuStatus = menuStatus;
        this.restaurant = restaurant;
        this.price = price;
        this.menuName = menuName;
        this.menuId = menuId;
    }

    // 상태 변경 메서드
    public void updateStatus(MenuStatus menuStatus) {
        this.menuStatus = menuStatus;
    }

    // 메뉴 정보 수정 메서드
    public void updateMenu(String menuName, Integer price) {
        this.menuName = menuName;
        this.price = price;
    }

    public void deleteMenu() {

        this.menuStatus = MenuStatus.DELETED;  // 상태를 DELETED로 변경
    }

}
