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

    public MemberRepositoryAdapter(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

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

    @Override
    public Optional<Member> findById(final Long id) {
        return memberJpaRepository.findByIdWithTeam(id);
    }

    @Override
    public void deleteById(final Long id) {
        if (!memberJpaRepository.existsById(id)) {
            throw new IllegalArgumentException("Member not found with id: " + id);
        }
        memberJpaRepository.deleteById(id);
    }

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll();
    }

    @Override
    public List<Member> findAllWithTeamUsingFetchJoin() {
        return memberJpaRepository.findAllWithTeamUsingFetchJoin();
    }

    @Override
    public boolean existsById(final Long id) {
        return memberJpaRepository.existsById(id);
    }

    @Override
    public List<Member> findByTeamId(final Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID must not be null");
        }
        return memberJpaRepository.findByTeamId(teamId);
    }

}
