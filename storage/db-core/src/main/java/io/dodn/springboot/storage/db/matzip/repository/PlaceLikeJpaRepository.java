package io.dodn.springboot.storage.db.matzip.repository;

import io.dodn.springboot.storage.db.matzip.entity.PlaceLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlaceLikeJpaRepository extends JpaRepository<PlaceLike, Long> {

    // 특정 사용자가 주어진 여러 맛집 ID들 중에서 좋아요를 누른 맛집의 ID 목록만 조회
    // 반환 타입을 Set으로 하면 중복 없이 빠르게 '포함 여부'를 확인할 수 있습니다.
    @Query("SELECT pl.place.id FROM PlaceLike pl WHERE pl.member.id = :memberId AND pl.place.id IN :placeIds")
    Set<Long> findLikedPlaceIdsByMemberAndPlaceIds(@Param("memberId") Long memberId, @Param("placeIds") List<Long> placeIds);

    Optional<PlaceLike> findByMemberIdAndPlaceId(Long memberId, Long placeId);

    void deleteByMemberIdAndPlaceId(Long memberId, Long placeId);
}
