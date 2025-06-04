package io.dodn.springboot.auth.kakao.client;

import io.dodn.springboot.auth.kakao.dto.KakaoUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoUserApiClient", url = "https.kapi.kako.com")
public interface KakaoUserApiClient {
    @PostMapping(value = "/v2/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoUserInfoResponse getKakaoUserInfo(
            @RequestHeader("Authorization") String accessToken
    );
}
