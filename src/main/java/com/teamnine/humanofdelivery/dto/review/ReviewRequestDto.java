package com.teamnine.humanofdelivery.dto.review;



import com.teamnine.humanofdelivery.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

    @NotNull
    private Long orderId;

    private String content;

    @Min(1)
    @Max(5)
    private int rate;

    public Review toEntity() {
        return Review.builder()
                .content(this.content)
                .rate(this.rate)
                .build();

    }
}
