package io.dodn.springboot.common.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final static Logger log = LoggerFactory.getLogger(LoginUserArgumentResolver.class);
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);

        boolean isLongType = Long.class.isAssignableFrom(parameter.getParameterType()) ||
                long.class.isAssignableFrom(parameter.getParameterType());
        log.info("supportsParameter result : {}", hasLoginUserAnnotation && isLongType);
        return hasLoginUserAnnotation && isLongType;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.debug("@LoginUser: Authentication is null or not authenticated.");
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof final UserDetails userDetails) { // UserDetails 타입인지 확인
            String username = userDetails.getUsername(); // JWT의 subject (memberId)
            try {
                return Long.parseLong(username);
            } catch (NumberFormatException e) {
                log.error("@LoginUser: Cannot parse user ID from UserDetails username: {}", username, e);
                return null;
            }
        } else if (principal instanceof String) { // 기존 로직 유지 (다른 인증 방식 호환성)
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                log.error("@LoginUser: Cannot parse user ID from String principal: {}", principal, e);
                return null;
            }
        } else if (principal instanceof Long) { // 기존 로직 유지
            return principal;
        }

        log.warn("@LoginUser: Unsupported principal type: {}. Principal: {}", principal.getClass().getName(), principal);
        return null;
    }
}