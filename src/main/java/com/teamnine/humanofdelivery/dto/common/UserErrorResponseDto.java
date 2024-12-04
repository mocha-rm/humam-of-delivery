package com.teamnine.humanofdelivery.dto.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class UserErrorResponseDto {
    private final HttpStatus httpStatus;
    private final String message;
}