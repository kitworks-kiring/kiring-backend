package io.dodn.springboot.member.domain;

import io.dodn.springboot.member.exception.NotFoundMemberException;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundMemberException("Member not found with id: " + id));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Member> findAllMembersWithTeamUsingFetchJoin() {
        return memberRepository.findAllWithTeamUsingFetchJoin();
    }

    public Long countMembers() {
        return memberRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Member> findAllMemberByTeamId(final Long teamId) {
        return memberRepository.findMembersAndFetchTeamByTeamId(teamId);
    }

    @Transactional
    public Member updateMember(final Long memberId, final Member member) {
        Member existingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("ID가 " + memberId + "인 멤버를 찾을 수 없습니다."));

        existingMember.updateProfile(member);

        return memberRepository.save(existingMember);
    }
}
