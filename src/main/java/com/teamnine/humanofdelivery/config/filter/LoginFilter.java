package com.teamnine.humanofdelivery.config.filter;

import com.teamnine.humanofdelivery.common.SessionNames;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {
    private static final String[] WHITE_LIST = {"/session", "/members/signup", "/members/login"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        log.info("로그인 필터 로직 실행");

        //WHITE LIST에 포함된 경우 true -> !true -> false
        if(!isWhiteList(requestURI)) {
            HttpSession session = httpRequest.getSession(false);

            if (session == null || session.getAttribute(SessionNames.USER_AUTH) == null) {
                throw new RuntimeException("로그인 해주세요");
            }
            // 로그인 성공 로직
            log.info("로그인에 성공했습니다.");
        }

        // 1번 경우 : WHITE LIST에 등록된 USR 요청이라면 chain.doFilter() 호출
        // 2번 경우 : WHITE LIST가 아닌 경우 위 필터로직을 통과 후에 chain.doFilter() 다음 필터나 Servlet 호출
        // 다음 필터가 없으면 Servlet -> Controller, 다음필터가 있으면 다음 Filter를 호출한다.
        filterChain.doFilter(request, response);
    }
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}