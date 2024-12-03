package com.teamnine.humanofdelivery.config;

import com.teamnine.humanofdelivery.config.interceptor.OwnerRoleInterceptor;
import com.teamnine.humanofdelivery.config.interceptor.UserRoleInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // todo path 협의 후 수정 필요
    private static final String[] OWNER_ROLE_REQUIRED_PATH_PATTERNS = {"/owner/*"};
    private static final String[] USER_ROLE_REQUIRED_PATH_PATTERNS = {"/user/*"};

    private final OwnerRoleInterceptor ownerRoleInterceptor;
    private final UserRoleInterceptor userRoleInterceptor;

    /**
     * <p>인터셉터의 우선순위와 path 패턴을 적용.</p>
     * {@inheritDoc}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ownerRoleInterceptor)
                .addPathPatterns(OWNER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE);

        registry.addInterceptor(userRoleInterceptor)
                .addPathPatterns(USER_ROLE_REQUIRED_PATH_PATTERNS)
                .order(Ordered.HIGHEST_PRECEDENCE + 1);
    }
}