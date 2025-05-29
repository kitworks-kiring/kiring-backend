package io.dodn.springboot.storage.db.member;

import io.dodn.springboot.storage.db.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    /**
 * 주어진 Member 엔티티를 저장하고, 저장된 인스턴스를 반환합니다.
 *
 * @param member 저장할 Member 엔티티
 * @return 저장된 Member 엔티티
 */
Member save(Member member);

    /**
 * 주어진 ID에 해당하는 회원 엔티티를 조회합니다.
 *
 * @param id 조회할 회원의 고유 식별자
 * @return 회원이 존재하면 해당 Member를 포함한 Optional, 존재하지 않으면 빈 Optional
 */
Optional<Member> findById(Long id);

    /****
 * 지정된 ID를 가진 회원 엔티티를 삭제합니다.
 *
 * @param id 삭제할 회원의 고유 식별자
 */
void deleteById(Long id);

    /**
 * 모든 회원 엔티티의 목록을 반환합니다.
 *
 * @return 전체 회원 엔티티 리스트
 */
List<Member> findAll();

    /**
 * 모든 회원 엔티티와 그에 연관된 팀 정보를 fetch join을 사용하여 조회합니다.
 *
 * @return 팀 정보가 포함된 모든 회원 엔티티의 리스트
 */
List<Member> findAllWithTeamUsingFetchJoin();

    /**
 * 지정된 ID를 가진 회원 엔티티가 존재하는지 확인합니다.
 *
 * @param id 존재 여부를 확인할 회원의 고유 식별자
 * @return 해당 ID의 회원이 존재하면 true, 그렇지 않으면 false
 */
boolean existsById(Long id);

    /**
 * 지정된 팀 ID에 속한 모든 회원 엔티티 목록을 반환합니다.
 *
 * @param teamId 조회할 팀의 고유 ID
 * @return 해당 팀에 속한 회원 엔티티 리스트
 */
List<Member> findByTeamId(Long teamId);

    /**
 * 지정된 팀 ID에 속한 모든 멤버와 해당 팀 정보를 함께 조회합니다.
 *
 * @param teamId 조회할 팀의 고유 ID
 * @return 팀 ID에 해당하는 멤버와 그들의 팀 정보가 포함된 리스트
 */
List<Member> findMembersAndFetchTeamByTeamId(Long teamId);
}
