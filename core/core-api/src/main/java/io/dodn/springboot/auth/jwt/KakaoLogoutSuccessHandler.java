package io.dodn.springboot.auth.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class KakaoLogoutSuccessHandler implements LogoutSuccessHandler {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${kakao.logout.uri}")
    private String kakaoLogoutUri;
    @Value("${kakao.logout.client-logout-redirect-uri}")
    private String appLogoutRedirectUri;

    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        String kakaoLogoutUrl = UriComponentsBuilder
                .fromUriString(kakaoLogoutUri)
                .queryParam("client_id", kakaoClientId)
                .queryParam("logout_redirect_uri", appLogoutRedirectUri)
                .build()
                .encode()
                .toUriString();

        response.sendRedirect(kakaoLogoutUrl);
    }
}
