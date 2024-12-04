package com.teamnine.humanofdelivery.entity;

import com.teamnine.humanofdelivery.StoreStatus;
import com.teamnine.humanofdelivery.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Entity
@Table(name = "stores")
@NoArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private StoreStatus status;

    @Column(nullable = false, name = "price_atleast")
    private Integer minCost;

    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;

    //TODO : 유저식별자



    @Builder
    public Store(String name, StoreStatus storeStatus, Integer minCost, LocalTime openAt, LocalTime closeAt) {
        this.name = name;
        this.status = storeStatus;
        this.minCost = minCost;
        this.openAt = openAt;
        this.closeAt = closeAt;
    }

    public void patchStatus(StoreStatus storeStatus) {
        this.status = storeStatus;
    }
}
