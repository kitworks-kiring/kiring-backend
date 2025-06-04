package io.dodn.springboot.auth.kakao.service;

import io.dodn.springboot.auth.kakao.client.KakaoAuthClient;
import io.dodn.springboot.auth.kakao.client.KakaoUserApiClient;
import io.dodn.springboot.auth.kakao.dto.KakaoTokenResponse;
import io.dodn.springboot.auth.kakao.dto.KakaoUserInfoResponse;
import io.dodn.springboot.common.support.error.CoreException;
import io.dodn.springboot.common.support.error.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class KakaoOAuthService {
    private static final Logger log = LoggerFactory.getLogger(KakaoOAuthService.class);
    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoUserApiClient kakaoUserApiClient;

    public KakaoOAuthService(final KakaoAuthClient kakaoAuthClient, final KakaoUserApiClient kakaoUserApiClient) {
        this.kakaoAuthClient = kakaoAuthClient;
        this.kakaoUserApiClient = kakaoUserApiClient;
    }

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private static final String KAKAKO_GRANT_TYPE = "authorization_code";

    public KakaoTokenResponse getKakaoToken(String code) {
        try {
            return kakaoAuthClient.getKakaoToken(
                    KAKAKO_GRANT_TYPE,
                    clientId,
                    redirectUri,
                    code
            );
        } catch (Exception e){
            log.error("Failed to get Kakao token: {}", e.getMessage(), e);
            throw new CoreException(ErrorType.FAILED_KAKAO, e);
        }
    }


    public KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
        try {
            return kakaoUserApiClient.getKakaoUserInfo("Bearer " + accessToken);
        } catch (Exception e) {
            log.error("Failed to get Kakao user info: {}", e.getMessage(), e);
            throw new CoreException(ErrorType.FAILED_KAKAO, e);
        }
    }


}
