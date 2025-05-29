package io.dodn.springboot.storage.db.member.repository;

import io.dodn.springboot.storage.db.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m JOIN FETCH m.team WHERE m.id = :memberIdValue")
    Optional<Member> findByIdWithTeam(@Param("memberIdValue") Long id);

    @Query("SELECT m FROM Member m JOIN FETCH m.team")
    List<Member> findAllWithTeamUsingFetchJoin();

    List<Member> findByTeamId(Long teamId);

     @Query("SELECT m FROM Member m JOIN FETCH m.team t WHERE t.id = :teamId")
    List<Member> findMembersAndFetchTeamByTeamId(@Param("teamId") Long teamId);
}
