package io.dodn.springboot.member.controller.response;

import io.dodn.springboot.storage.db.member.entity.Member;

public record MemberResponse(Member member) {

    /**
     * 주어진 Member 객체로 새로운 MemberResponse 인스턴스를 생성합니다.
     *
     * @param member 응답에 포함할 Member 객체
     * @return 주어진 Member를 포함하는 MemberResponse 인스턴스
     */
    public static MemberResponse of(Member member) {
        return new MemberResponse(member);
    }

    /**
     * 캡슐화된 {@link Member} 객체를 반환합니다.
     *
     * @return 이 응답에 포함된 Member 인스턴스
     */
    public Member getMember() {
        return member;
    }
}
