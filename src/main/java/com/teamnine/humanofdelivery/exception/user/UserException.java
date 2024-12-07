package com.teamnine.humanofdelivery.exception.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserException extends RuntimeException {
    private final UserErrorCode userErrorCode;

    @Override
    public String getMessage() {
        return userErrorCode.getMessage();
    }
}