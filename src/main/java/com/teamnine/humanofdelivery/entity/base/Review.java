package com.teamnine.humanofdelivery.entity.base;


import com.teamnine.humanofdelivery.status.MenuStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Column(name ="restaurantId", nullable =false);
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Column(name ="orderId", nullable =false);
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    Column(name ="userId", nullable =false);
    private User user;

    @Column
    private String content;

    @Column
    private int rate;

    @Builder
    public Review(Long ReviewId, Restaurant restaurant, Order order, String content, int rate) {
        this.id = ReviewId;
        this.restaurant = restaurant;
        this.order = order;
        this.content = content;
        this.rate = rate;
    }

    // 메뉴 정보 수정 메서드
    public void updateReview(String content, Integer rate) {
        this.content = content;
        this.rate = rate;
    }

    public void deleteMenu(){

    }
}
