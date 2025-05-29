package io.dodn.springboot.storage.db.member.adapter;

import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import io.dodn.springboot.storage.db.member.repository.MemberJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    /**
     * MemberJpaRepository를 어댑터에 주입하여 MemberRepository 인터페이스 구현체를 생성합니다.
     *
     * @param memberJpaRepository 멤버 엔티티의 영속성 작업을 위임할 JPA 리포지토리
     */
    public MemberRepositoryAdapter(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    /**
     * 회원 정보를 저장하거나, 기존 회원의 이름과 이메일을 수정하여 저장합니다.
     *
     * 회원의 ID가 없으면 새 회원으로 저장하고, ID가 있으면 해당 회원을 찾아 이름과 이메일을 갱신한 뒤 저장합니다.
     * ID에 해당하는 회원이 존재하지 않을 경우 예외가 발생합니다.
     *
     * @param member 저장 또는 수정할 회원 엔티티
     * @return 저장된 회원 엔티티
     * @throws IllegalArgumentException 해당 ID의 회원이 존재하지 않을 경우 발생
     */
    @Override
    public Member save(final Member member) {
        if (member.getId() == null) {
            return memberJpaRepository.save(member);
        }
        else {
            return memberJpaRepository.findById(member.getId()).map(existingMember -> {
                existingMember.changeName(member.getName());
                existingMember.changeEmail(member.getEmail());
                return memberJpaRepository.save(existingMember);
            }).orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + member.getId()));
        }
    }

    /**
     * 주어진 ID로 회원과 연관된 팀 정보를 함께 조회합니다.
     *
     * @param id 조회할 회원의 ID
     * @return 회원과 팀 정보를 포함하는 Optional 객체. 해당 ID의 회원이 없으면 빈 Optional 반환
     */
    @Override
    public Optional<Member> findById(final Long id) {
        return memberJpaRepository.findByIdWithTeam(id);
    }

    /****
     * 주어진 ID의 회원을 삭제합니다.
     *
     * 회원이 존재하지 않을 경우 IllegalArgumentException이 발생합니다.
     *
     * @param id 삭제할 회원의 ID
     * @throws IllegalArgumentException 해당 ID의 회원이 존재하지 않을 때 발생
     */
    @Override
    public void deleteById(final Long id) {
        if (!memberJpaRepository.existsById(id)) {
            throw new IllegalArgumentException("Member not found with id: " + id);
        }
        memberJpaRepository.deleteById(id);
    }

    /**
     * 모든 회원 엔티티의 목록을 반환합니다.
     *
     * @return 전체 회원 목록
     */
    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll();
    }

    /**
     * 모든 회원과 그에 연관된 팀 정보를 fetch join을 사용하여 조회합니다.
     *
     * @return 회원과 팀 정보가 포함된 Member 객체 리스트
     */
    @Override
    public List<Member> findAllWithTeamUsingFetchJoin() {
        return memberJpaRepository.findAllWithTeamUsingFetchJoin();
    }

    /****
     * 주어진 ID를 가진 회원이 존재하는지 여부를 반환합니다.
     *
     * @param id 회원의 고유 식별자
     * @return 해당 ID의 회원이 존재하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean existsById(final Long id) {
        return memberJpaRepository.existsById(id);
    }

    /**
     * 지정된 팀 ID에 속한 모든 멤버 목록을 반환합니다.
     *
     * @param teamId 멤버를 조회할 팀의 ID
     * @return 해당 팀에 속한 멤버들의 리스트
     */
    @Override
    public List<Member> findByTeamId(final Long teamId) {
        return memberJpaRepository.findByTeamId(teamId);
    }

    /**
     * 주어진 팀 ID에 속한 모든 멤버와 해당 멤버들의 팀 정보를 함께 조회합니다.
     *
     * @param teamId 조회할 팀의 ID
     * @return 팀에 속한 멤버와 각 멤버의 팀 정보가 포함된 리스트
     */
    @Override
    public List<Member> findMembersAndFetchTeamByTeamId(final Long teamId) {
        return memberJpaRepository.findMembersAndFetchTeamByTeamId(teamId);
    }


}
