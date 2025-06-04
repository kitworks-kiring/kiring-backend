package io.dodn.springboot.auth.jwt.dto;

public record TokenInfo(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {
}
