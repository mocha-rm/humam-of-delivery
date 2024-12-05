package com.teamnine.humanofdelivery.entity;

import com.teamnine.humanofdelivery.OrderStatus;
import com.teamnine.humanofdelivery.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

//TODO : 유저 브랜치 병합 후 구현 예정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private Member user;

    private Long userId;

    @Column(nullable = false)
    private String menuName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    public Order(Store store, Long userId, String menuName, OrderStatus orderStatus) {
        this.store = store;
        this.userId = userId;
        this.menuName = menuName;
        this.orderStatus = orderStatus;
    }

    public void patchStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
