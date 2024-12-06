package com.teamnine.humanofdelivery.exception.store;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreErrorCode {
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
    STORE_ERROR_AUTHORIZATION_01(HttpStatus.UNAUTHORIZED, "일반 사용자는 가게를 오픈할 수 없습니다."),
    STORE_ERROR_AUTHORIZATION_02(HttpStatus.UNAUTHORIZED, "본인 가게만 폐업할 수 있습니다."),
    STORE_ERROR_OWNER_01(HttpStatus.FORBIDDEN, "폐업 상태가 아닌 가게를 3개까지 오픈할 수 있습니다."),
    STORE_ERROR_OWNER_02(HttpStatus.BAD_REQUEST, "이미 처리된 요청입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
