package io.dodn.springboot.member.domain;

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
            .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Member> findAllMembersWithTeamUsingFetchJoin() {
        return memberRepository.findAllWithTeamUsingFetchJoin();
    }

    public Long countMembers() {
        return (long) memberRepository.findAll().size();
    }

    @Transactional(readOnly = true)
    public List<Member> findAllMemberByTeamId(final Long teamId) {
        return memberRepository.findByTeamId(teamId);
    }

}
