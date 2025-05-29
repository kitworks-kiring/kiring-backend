package io.dodn.springboot.member.controller.response;

import io.dodn.springboot.storage.db.member.entity.Member;

import java.util.List;

public record MembersResponse(List<Member> members) {

    /**
     * 주어진 회원 목록을 포함하는 MembersResponse 인스턴스를 생성합니다.
     *
     * @param members 응답에 포함할 회원 목록
     * @return 지정된 회원 목록을 담은 MembersResponse 객체
     */
    public static MembersResponse of(List<Member> members) {
        return new MembersResponse(members);
    }

    /**
     * 캡슐화된 회원 목록을 반환합니다.
     *
     * @return Member 객체의 리스트
     */
    public List<Member> getMembers() {
        return members;
    }
}
