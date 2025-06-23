package io.dodn.springboot.plane.controller.response;

import io.dodn.springboot.storage.db.member.entity.Member;

public record RecommendedMemberResponse(
        Long id,
        String name,
        String profileImageUrl,
        String kiringImageUrl
) {
    public static RecommendedMemberResponse from(
            Member member
    ) {
        if (member == null) {
            return new RecommendedMemberResponse(null, null, null, null);
        }
        return new RecommendedMemberResponse(
                member.getId(),
                member.getName(),
                member.getProfileImageUrl(),
                member.getKiringImageUrl()
        );
    }
}
