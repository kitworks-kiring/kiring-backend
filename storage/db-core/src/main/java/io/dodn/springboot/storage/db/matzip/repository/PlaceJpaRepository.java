package io.dodn.springboot.storage.db.matzip.repository;

import io.dodn.springboot.storage.db.matzip.PlaceWithDistance;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceJpaRepository extends JpaRepository<Place, Long> {

    @Query(
            value = "SELECT count(*) FROM place p WHERE ST_Distance_Sphere(p.location, ST_PointFromText(:point, 4326)) <= :radius",
            nativeQuery = true
    )
    long countNearbyPlaces(@Param("point") String point, @Param("radius") int radius);

    @Query(
            value = "SELECT " +
                    " p.id AS placeId, p.name, p.address, p.address, p.description, " +
                    " ST_X(p.location) AS longitude, " +
                    " ST_Y(p.location) AS latitude, " +
                    " ST_Distance_Sphere(p.location, ST_PointFromText(:point, 4326)) AS distance " +
                    "FROM place p " +
                    "WHERE ST_Distance_Sphere(p.location, ST_PointFromText(:point, 4326)) <= :radius " +
                    "ORDER BY distance " +
                    "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}",
            nativeQuery = true
    )
    List<PlaceWithDistance> findNearbyPlaces(@Param("point") String point, @Param("radius") int radius, Pageable pageable);

}
