package com.teamnine.humanofdelivery.exception.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {
    RESPONSE_INCORRECT("400", "잘못된 입력값입니다."),
    EMAIL_DUPLICATE("409", "이미 사용 중인 이메일입니다."),
    EMAIL_INCORRECT("400", "이메일이 일치하지 않습니다."),
    EMAIL_NOT_FOUND("404", "존재하지 않는 이메일입니다."),
    PASSWORD_INCORRECT("400", "비밀번호가 일치하지 않습니다."),
    USER_DEACTIVATED("400", "이미 회원탈퇴 처리 되었습니다."),
    USER_NOT_FOUND("404", "존재하지 않는 회원입니다.");

    private final String code;
    private final String message;
}
