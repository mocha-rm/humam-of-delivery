package com.teamnine.humanofdelivery.config.interceptor;

import com.teamnine.humanofdelivery.common.SessionNames;
import com.teamnine.humanofdelivery.entity.User;
import com.teamnine.humanofdelivery.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.file.AccessDeniedException;

@Component
public class UserRoleInterceptor implements HandlerInterceptor {

    /**
     * 사장 권한을 확인합니다.
     *
     * @param request  {@code HttpServletRequest}
     * @param response {@code HttpServletResponse}
     * @param handler  실행하기 위해 선택된 핸들러
     * @return 인터셉터 체이닝 여부
     * <ul>
     *  <li>{@code true} - 다음 인터셉터 또는 핸들러를 실행.</li>
     *  <li>{@code false} - 다음 인터셉터를 실행하지 않고 중단.</li>
     * </ul>
     * @throws AccessDeniedException 인가되지 않았을 경우
     * @throws SecurityException 권한이 없을 경우
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler
    ) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AccessDeniedException("세션이 없습니다.");
        }

        User user = (User) session.getAttribute(SessionNames.USER_AUTH);
        UserRole role = user.getRole();

        if (role != UserRole.USER) {
            throw new SecurityException("USER 권한이 필요합니다.");
        }
        return true;
    }
}
