package com.teamnine.humanofdelivery.exception.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MenuErrorCode {
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),
    MENU_ERROR_AUTHORIZATION_01(HttpStatus.UNAUTHORIZED,"가게 보유자만 메뉴 생성 가능합니다."),
    MENU_ERROR_AUTHORIZATION_02(HttpStatus.UNAUTHORIZED,"가게 보유자만 수정 가능합니다."),
    MENU_ERROR_AUTHORIZATION_03(HttpStatus.UNAUTHORIZED,"가게 보유자만 삭제가 가능합니다."),
    MENU_ERROR_NAME_01(HttpStatus.BAD_REQUEST, "같은 이름의 메뉴는 생성할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
