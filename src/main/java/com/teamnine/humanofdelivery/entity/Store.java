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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @Column(nullable = false, name = "price_atleast")
    private Integer minCost;

    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;


    @Builder
    public Store(String name, Member member, StoreStatus storeStatus, Integer minCost, LocalTime openAt, LocalTime closeAt) {
        this.name = name;
        this.member = member;
        this.status = storeStatus;
        this.minCost = minCost;
        this.openAt = openAt;
        this.closeAt = closeAt;
    }

    public void patchStatus(StoreStatus storeStatus) {
        this.status = storeStatus;
    }
}
