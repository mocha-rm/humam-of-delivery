package com.teamnine.humanofdelivery.config.interceptor;

import com.teamnine.humanofdelivery.common.SessionNames;
import com.teamnine.humanofdelivery.config.role.MemberRole;
import com.teamnine.humanofdelivery.exception.user.UserErrorCode;
import com.teamnine.humanofdelivery.exception.user.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
     * @throws UserException 권한이 없을 경우 예외처리
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler
    ) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new UserException(UserErrorCode.LOGIN_REQUIRED);
        }

        MemberRole role = (MemberRole) session.getAttribute(SessionNames.USER_ROLE);
        if (role != MemberRole.USER) {
            throw new UserException(UserErrorCode.PERMISSION_DENIED);
        }
        return true;
    }
}
