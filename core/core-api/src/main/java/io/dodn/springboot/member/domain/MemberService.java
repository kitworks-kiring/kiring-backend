package io.dodn.springboot.member.domain;

import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * MemberService를 주어진 MemberRepository로 초기화합니다.
     *
     * @param memberRepository 멤버 엔티티의 데이터 접근을 위한 리포지토리
     */
    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 새로운 회원 엔티티를 저장하고 저장된 회원을 반환합니다.
     *
     * @param member 저장할 회원 엔티티
     * @return 저장된 회원 엔티티
     */
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    /**
     * 주어진 ID로 회원을 조회합니다.
     *
     * @param id 조회할 회원의 ID
     * @return 해당 ID를 가진 회원 엔티티
     * @throws IllegalArgumentException 해당 ID의 회원이 존재하지 않을 경우 발생합니다.
     */
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));
    }

    /**
     * 주어진 ID에 해당하는 회원 엔티티를 삭제합니다.
     *
     * @param id 삭제할 회원의 ID
     */
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    /**
     * 모든 회원과 그에 연관된 팀 정보를 fetch join을 사용하여 조회합니다.
     *
     * @return 팀 정보가 함께 포함된 모든 회원 목록
     */
    @Transactional(readOnly = true)
    public List<Member> findAllMembersWithTeamUsingFetchJoin() {
        return memberRepository.findAllWithTeamUsingFetchJoin();
    }

    /**
     * 전체 회원 수를 반환합니다.
     *
     * @return 등록된 회원의 총 개수
     */
    public Long countMembers() {
        return (long) memberRepository.findAll().size();
    }

    /**
     * 지정된 팀 ID에 속한 모든 회원과 해당 팀 정보를 즉시 로딩하여 반환합니다.
     *
     * @param teamId 조회할 팀의 ID
     * @return 팀 ID에 해당하는 회원 목록
     */
    @Transactional(readOnly = true)
    public List<Member> findAllMemberByTeamId(final Long teamId) {
        return memberRepository.findMembersAndFetchTeamByTeamId(teamId);
    }

}
