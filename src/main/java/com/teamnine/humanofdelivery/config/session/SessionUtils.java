package com.teamnine.humanofdelivery.config.session;

import com.teamnine.humanofdelivery.common.SessionNames;
import com.teamnine.humanofdelivery.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class SessionUtils {
    private final HttpSession session;

    // 이름 가져오기
    public String getLoginUserName() {
        String name = (String) session.getAttribute(SessionNames.USER_AUTH);
        if (name == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return name;
    }

    // 이메일 가져오기
    public String getLoginUserEmail() {
        String email = (String)session.getAttribute(SessionNames.USER_AUTH);

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return email;
    }

    // 리소스 소유자와 현재 로그인 사용자 비교하여 권한 확인
    public void checkAuthorization(Member resourceOwner) {
        String loginEmail = getLoginUserEmail(); // 현재 로그인된 사용자 이메일
        String ownerEmail = resourceOwner.getEmail(); // 리소스 소유자의 이메일

        if (!loginEmail.equals(ownerEmail)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        }
    }
    public void reloadSession(String email) {
        session.setAttribute("sessionKey", email);
    }
}
