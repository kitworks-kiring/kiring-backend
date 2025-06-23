package io.dodn.springboot.auth.controller;

import io.dodn.springboot.auth.controller.request.RefreshRequest;
import io.dodn.springboot.auth.domain.AuthService;
import io.dodn.springboot.auth.jwt.dto.TokenInfo;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "액세스 토큰 재발급", description = "만료된 액세스 토큰 대신 리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받습니다.", tags = { "Auth Controller" })
    @PostMapping("/refresh")
    public ApiResponse<TokenInfo> refreshToken(@RequestBody RefreshRequest request) {
        TokenInfo newTokenInfo = authService.refreshToken(request.refreshToken());
        return ApiResponse.success(newTokenInfo);
    }

}
