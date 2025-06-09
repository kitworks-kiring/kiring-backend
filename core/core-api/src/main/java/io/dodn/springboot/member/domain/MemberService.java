package io.dodn.springboot.member.domain;

import io.dodn.springboot.auth.kakao.dto.KakaoUserInfoResponse;
import io.dodn.springboot.member.exception.NotFoundMemberException;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
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

    @Transactional
    public Member findOrCreateMemberByKakaoInfo(KakaoUserInfoResponse kakaoUserInfoResponse){
        log.info(kakaoUserInfoResponse.toString());
        log.info(changePhoneNumber(kakaoUserInfoResponse.kakaoAccount().phoneNumber()));
        return memberRepository.findByPhone(changePhoneNumber(kakaoUserInfoResponse.kakaoAccount().phoneNumber()))
                .orElseThrow(
                        () -> new NotFoundMemberException("가입되지 않은 유저입니다. : " + kakaoUserInfoResponse.kakaoAccount().phoneNumber())
                );
    }

    private String changePhoneNumber(String phoneNumber) {
        return "0".concat(phoneNumber.substring(4));
    }
}
