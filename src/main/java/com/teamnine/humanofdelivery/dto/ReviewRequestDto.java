package com.teamnine.humanofdelivery.dto;


import com.teamnine.humanofdelivery.entity.base.Menu;
import com.teamnine.humanofdelivery.entity.base.Review;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

    private String content;

    @NotNull
    private int rate;

    public Review toEntity() {
        return Review.builder()
                .content(this.content)
                .rate(this.rate)
                .build();

    }
}
