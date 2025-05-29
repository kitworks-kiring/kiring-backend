package io.dodn.springboot.member.controller.response;

import io.dodn.springboot.storage.db.member.entity.Member;

public record MemberResponse(Member member) {

    public static MemberResponse of(Member member) {
        return new MemberResponse(member);
    }

    public Member getMember() {
        return member;
    }
}
