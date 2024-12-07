package com.teamnine.humanofdelivery.exception.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인 해주세요."),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    RESPONSE_INCORRECT(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    EMAIL_INCORRECT(HttpStatus.BAD_REQUEST, "이메일이 일치하지 않습니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USER_DEACTIVATED(HttpStatus.BAD_REQUEST, "이미 회원탈퇴 처리 되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
