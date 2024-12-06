package com.teamnine.humanofdelivery.exception.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
    ORDER_ERROR_AUTHORIZATION_01(HttpStatus.UNAUTHORIZED,"주문은 일반 사용자 계정을 이용해주세요."),
    ORDER_ERROR_AUTHORIZATION_02(HttpStatus.UNAUTHORIZED,"주문상태를 변경할 권한이 없습니다."),
    ORDER_ERROR_AUTHORIZATION_03(HttpStatus.UNAUTHORIZED, "본인 가게의 주문이 아니므로 상태변경이 제한됩니다."),
    ORDER_ERROR_OWNER_01(HttpStatus.BAD_REQUEST, "같은 상태로는 변경할 수 없습니다."),
    ORDER_ERROR_USER_01(HttpStatus.NOT_ACCEPTABLE, "영업시간 외에는 주문할 수 없습니다."),
    ORDER_ERROR_USER_02(HttpStatus.NOT_ACCEPTABLE, "최소 주문금액을 만족하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
