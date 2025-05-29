package io.dodn.springboot.storage.db.member.repository;

import io.dodn.springboot.storage.db.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    /**
     * 주어진 ID에 해당하는 회원과 그에 연관된 팀 정보를 함께 조회합니다.
     *
     * @param id 조회할 회원의 ID
     * @return 회원과 팀 정보가 포함된 Optional<Member> 객체
     */
    @Query("SELECT m FROM Member m JOIN FETCH m.team WHERE m.id = :memberIdValue")
    Optional<Member> findByIdWithTeam(@Param("memberIdValue") Long id);

    /**
     * 모든 회원 엔티티와 연관된 팀 정보를 페치 조인으로 함께 조회합니다.
     *
     * @return 팀 정보가 함께 로드된 Member 엔티티 리스트
     */
    @Query("SELECT m FROM Member m JOIN FETCH m.team")
    List<Member> findAllWithTeamUsingFetchJoin();

    /****
 * 지정된 팀 ID에 속한 모든 회원 엔티티를 조회합니다.
 *
 * @param teamId 조회할 팀의 ID
 * @return 해당 팀에 속한 회원 엔티티 목록
 */
List<Member> findByTeamId(Long teamId);

     /**
     * 지정된 팀 ID에 속한 모든 회원과 해당 팀 정보를 함께 조회합니다.
     *
     * @param teamId 조회할 팀의 ID
     * @return 팀 ID에 해당하는 회원과 그들의 팀 정보 목록
     */
    @Query("SELECT m FROM Member m JOIN FETCH m.team t WHERE t.id = :teamId")
    List<Member> findMembersAndFetchTeamByTeamId(@Param("teamId") Long teamId);
}
