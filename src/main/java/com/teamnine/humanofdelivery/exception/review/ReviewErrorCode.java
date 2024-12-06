package com.teamnine.humanofdelivery.exception.review;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),
    REVIEW_ERROR_AUTHORIZATION_01(HttpStatus.UNAUTHORIZED,"리뷰 작성은 주문자만 가능합니다."),
    REVIEW_ERROR_AUTHORIZATION_02(HttpStatus.UNAUTHORIZED,"리뷰 수정은 주문자만 가능합니다."),
    REVIEW_ERROR_AUTHORIZATION_03(HttpStatus.UNAUTHORIZED,"리뷰 삭제는 주문자만 가능합니다."),
    REVIEW_ERROR_NAME_01(HttpStatus.BAD_REQUEST, "주문에는 하나의 리뷰만 작성 가능합니다."),
    REVIEW_ERROR_STATUS_01(HttpStatus.NOT_ACCEPTABLE, "배달 완료된 주문만 리뷰 작성 가능합니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
