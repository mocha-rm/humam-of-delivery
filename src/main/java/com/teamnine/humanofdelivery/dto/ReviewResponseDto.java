package com.teamnine.humanofdelivery.dto;


import com.teamnine.humanofdelivery.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private String content;
    private int rate;
    private Long storeId;
    private Long userId;
    private Long orderId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rate = review.getRate();
        this.storeId = review.getStore().getId();
        this.userId= review.getUser().getUserId();
        this.orderId = review.getOrder().getId();
        this.createdAt= review.getCreatedDate();
        this.modifiedAt= review.getModifiedDate();

    }
}
