package io.dodn.springboot.member.controller.response;

import io.dodn.springboot.storage.db.member.entity.Member;

import java.util.List;

public record MembersResponse(List<Member> members) {

    public static MembersResponse of(List<Member> members) {
        return new MembersResponse(members);
    }

    public List<Member> getMembers() {
        return members;
    }
}
