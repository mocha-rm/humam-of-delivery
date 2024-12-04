package com.teamnine.humanofdelivery.dto;


import com.teamnine.humanofdelivery.entity.base.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewId;
    private String content;
    private int rate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getId();
        this.content = review.getContent();
        this.rate = review.getRate();

    }
}
