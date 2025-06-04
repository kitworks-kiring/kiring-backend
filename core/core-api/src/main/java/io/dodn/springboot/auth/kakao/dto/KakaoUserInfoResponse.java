package io.dodn.springboot.auth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponse(
        Long id,
        @JsonProperty("connected_at") String connectedAt,
        @JsonProperty("kakao_account") KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
            @JsonProperty("profile_needs_agreement") Boolean profileNeedsAgreement,
            @JsonProperty("email_needs_agreement") Boolean emailNeedsAgreement,
            @JsonProperty("birthday_needs_agreement") Boolean birthdayNeedsAgreement,
            @JsonProperty("is_email_valid") Boolean isEmailValid,
            @JsonProperty("is_email_verified") Boolean isEmailVerified,

            Profile profile,
            // ... 기타 필요한 필드 (birthday, gender 등)
            String name,
            String email,
            @JsonProperty("age_range") String ageRange,
            @JsonProperty("phone_number") String phoneNumber,
            String birthyear,
            String birthday
    ) {
    }

    public record Profile(
            String nickname,
            @JsonProperty("thumbnail_image_url") String thumbnailImageUrl,
            @JsonProperty("profile_image_url") String profileImageUrl,
            @JsonProperty("is_default_image") Boolean isDefaultImage
    ) {
    }

    // 편의 메서드
    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.email() : null;
    }

    public String getNickname() {
        return kakaoAccount != null && kakaoAccount.profile() != null ? kakaoAccount.profile().nickname() : null;
    }

    public String getProfileImageUrl() {
        return kakaoAccount != null && kakaoAccount.profile() != null ? kakaoAccount.profile().profileImageUrl() : null;
    }
}
    