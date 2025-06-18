package io.dodn.springboot.member.controller.response;

import io.dodn.springboot.storage.db.member.entity.Member;

public record ImageUrlResponse(
        String kiringImageUrl,
        String profileImageUrl
) {
    public static ImageUrlResponse of(Member member) {
        return new ImageUrlResponse(member.getKiringImageUrl(), member.getProfileImageUrl());
    }

    public String getKiringImageUrl() {
        return kiringImageUrl;
    }
}
