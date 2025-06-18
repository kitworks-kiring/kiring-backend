package io.dodn.springboot.plane.controller.response;

import io.dodn.springboot.storage.db.member.entity.Member;

public record SenderResponse(
        Long id,
        String name,
        String profileImageUrl
) {
    public static SenderResponse from(Member member) {
        return new SenderResponse(
                member.getId(),
                member.getNickname(),
                member.getProfileImageUrl()
        );
    }
}
