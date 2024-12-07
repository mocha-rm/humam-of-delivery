package com.teamnine.humanofdelivery.exception.review;

import com.teamnine.humanofdelivery.exception.order.OrderErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewException extends RuntimeException {
    private final ReviewErrorCode reviewErrorCode;

    @Override
    public String getMessage() {
        return reviewErrorCode.getMessage();
    }
}
