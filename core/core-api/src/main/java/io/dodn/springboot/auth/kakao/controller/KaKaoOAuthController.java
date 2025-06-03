package io.dodn.springboot.auth.kakao.controller;

import io.dodn.springboot.auth.AuthService;
import io.dodn.springboot.auth.jwt.dto.TokenInfo;
import io.dodn.springboot.common.support.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth/kakao")
public class KaKaoOAuthController {
    private final AuthService authService;

    public KaKaoOAuthController(AuthService authService) {
        this.authService = authService;
    }

    //localhost:8080/api/v1/oauth/kakao/callback
    @GetMapping("/callback")
    public ApiResponse<TokenInfo> kakaoCallback(@RequestParam("code") final String code) {
        // 카카오 OAuth 인증 후 받은 코드를 사용하여 토큰을 요청
        TokenInfo tokenInfo = authService.loginWithKakao(code);
        return ApiResponse.success(tokenInfo);
    }



}
