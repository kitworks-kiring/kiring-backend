package io.dodn.springboot.member.controller.request;

import io.dodn.springboot.storage.db.member.entity.Member;

public record UpdateMemberRequest(
        String name,
        String email,
        String phone,
        String kakaoId,
        String nickname,
        String profileImageUrl,
        String kiringImageUrl,
        String birthday,
        String githubId,
        boolean isEmployed,
        boolean isAdmin
) {
    public Member toEntity() {
        return new Member(
                name,
                email,
                phone,
                kakaoId,
                nickname,
                profileImageUrl,
                kiringImageUrl,
                birthday,
                githubId,
                isEmployed,
                isAdmin,
                null,
                null // 팀 정보는 별도로 설정하지 않음
        );
    }
}
