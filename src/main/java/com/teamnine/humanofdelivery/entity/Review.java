package com.teamnine.humanofdelivery.entity;


import com.teamnine.humanofdelivery.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="store_id", nullable =false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable =false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)//One to One으로 수정
    @JoinColumn (name ="order_id", nullable =false)
    private Order order;

    @Column
    private String content;

    @Column
    private int rate;

    @Builder
    public Review(Long ReviewId, Store store,User user, Order order, String content, int rate) {
        this.id = ReviewId;
        this.store = store;
        this.user = user;
        this.order = order;
        this.content = content;
        this.rate = rate;
    }

    // 메뉴 정보 수정 메서드
    public void updateReview(String content, Integer rate) {
        this.content = content;
        this.rate = rate;
    }

}
