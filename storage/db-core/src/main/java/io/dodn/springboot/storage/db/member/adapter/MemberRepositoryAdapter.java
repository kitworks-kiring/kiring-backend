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
        return memberJpaRepository.save(member);
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
        return memberJpaRepository.findByTeamId(teamId);
    }

    @Override
    public List<Member> findMembersAndFetchTeamByTeamId(final Long teamId) {
        return memberJpaRepository.findMembersAndFetchTeamByTeamId(teamId);
    }

    @Override
    public long count() {
        return memberJpaRepository.count();
    }

    @Override
    public Optional<Member> findByPhone(final String s) {
        return memberJpaRepository.findByPhone(s);
    }

    @Override
    public List<Member> findMembersWithBirthdayInMonth(final String monthString) {
        return memberJpaRepository.findMembersWithBirthdayInMonth(monthString);
    }


}
