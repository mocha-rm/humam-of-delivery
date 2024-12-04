package com.teamnine.humanofdelivery.entity;


import com.teamnine.humanofdelivery.entity.base.BaseEntity;
import com.teamnine.humanofdelivery.status.MenuStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "menuStatus", nullable = false)
    private MenuStatus menuStatus;


    @Builder
    public Menu(Long menuId, Store store, String menuName, Integer price, MenuStatus menuStatus) {
        this.menuStatus = menuStatus;
        this.store = store;
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
