package io.dodn.springboot.storage.db.matzip.repository;

import io.dodn.springboot.storage.db.matzip.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceJpaRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {
    // 공통 SELECT 구문 정의 (중복 제거)
    String SELECT_CLAUSE = "SELECT " +
            "p.id AS placeId, p.name, p.address, p.phone_number AS phoneNumber, p.like_count AS likeCount, p.kiring_category AS kiringCategory, " +
            "ST_X(p.location) AS longitude, ST_Y(p.location) AS latitude, " +
            "ST_Distance_Sphere(p.location, ST_PointFromText(:point, 4326)) AS distance ";
    String FROM_WHERE_CLAUSE = "FROM place p WHERE ST_Distance_Sphere(p.location, ST_PointFromText(:point, 4326)) <= :radius ";


    @Query(
            value = "SELECT count(*) FROM place p WHERE ST_Distance_Sphere(p.location, ST_PointFromText(:point, 4326)) <= :radius",
            nativeQuery = true
    )
    long countNearbyPlaces(@Param("point") String point, @Param("radius") int radius);

    @Query(value = "SELECT DISTINCT p FROM Place p LEFT JOIN FETCH p.categories",
            countQuery = "SELECT COUNT(p) FROM Place p")
    Page<Place> findAllWithCategories(Pageable pageable);

    @Modifying
    @Query("UPDATE Place p SET p.likeCount = p.likeCount + 1 WHERE p.id = :placeId")
    void increaseLikeCount(Long aLong);

    @Modifying // DML 쿼리임을 나타냄
    @Query("UPDATE Place p SET p.likeCount = p.likeCount - 1 WHERE p.id = :placeId AND p.likeCount > 0")
    void decreaseLikeCount(Long aLong);


//    // 거리순 정렬 (기본)
//    @Query(
//            value = SELECT_CLAUSE + FROM_WHERE_CLAUSE +
//                    "ORDER BY distance ASC, p.like_count DESC ",
//            nativeQuery = true
//    )
//    List<PlaceWithDistance> findNearbyPlacesOrderByDistance(@Param("point") String point, @Param("radius") int radius, Pageable pageable);
//
//    // 좋아요순 정렬
//    @Query(
//            value = SELECT_CLAUSE + FROM_WHERE_CLAUSE +
//                    "ORDER BY p.like_count DESC, distance ASC " ,
//            nativeQuery = true
//    )
//    List<PlaceWithDistance> findNearbyPlacesOrderByLikeCount(@Param("point") String point, @Param("radius") int radius, Pageable pageable);
//
//    // 최신순 정렬
//    @Query(
//            value = SELECT_CLAUSE + FROM_WHERE_CLAUSE +
//                    "ORDER BY p.id desc, distance ASC " ,
//            nativeQuery = true
//    )
//    List<PlaceWithDistance> findNearbyPlacesOrderByName(@Param("point") String point, @Param("radius") int radius, Pageable pageable);

}
