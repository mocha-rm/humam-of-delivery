package com.teamnine.humanofdelivery.exception.menu;

import com.teamnine.humanofdelivery.exception.order.OrderErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MenuException extends RuntimeException {
    private final MenuErrorCode menuErrorCode;

    @Override
    public String getMessage() {
        return menuErrorCode.getMessage();
    }
}
