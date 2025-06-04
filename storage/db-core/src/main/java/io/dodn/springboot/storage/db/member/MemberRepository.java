package io.dodn.springboot.storage.db.member;

import io.dodn.springboot.storage.db.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    void deleteById(Long id);

    List<Member> findAll();

    List<Member> findAllWithTeamUsingFetchJoin();

    boolean existsById(Long id);

    List<Member> findByTeamId(Long teamId);

    List<Member> findMembersAndFetchTeamByTeamId(Long teamId);

    long count();

    Optional<Member> findByPhone(String s);
}
