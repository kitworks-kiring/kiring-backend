package io.dodn.springboot.storage.db.matzip;

import io.dodn.springboot.storage.db.matzip.dto.PlaceNearbyDto;
import io.dodn.springboot.storage.db.matzip.entity.Category;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.entity.PlaceLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface MatzipRepository {
    Page<Place> findAll(Pageable pageable);

    Set<Long> findLikedPlaceIdsByMemberAndPlaceIds(Long memberId, List<Long> placeIds);

    Optional<PlaceLike> findByMemberIdAndPlaceId(Long memberId, Long placeId);

    Optional<Place> findByPlaceId(Long placeId);

    void delete(PlaceLike placeLike);

    void save(PlaceLike newLike);

    long countNearbyPlaces(String pointWkt, int radius);

    void saveAll(List<Place> placesToSave);

    Category saveCategory(Category newCategory);

    List<Category> categoryFindByNameIn(List<String> categoryNames);

    Page<Place> findAllWithCategories(Pageable pageable);

//    List<PlaceWithDistance> findNearbyPlacesOrderByDistance(String pointWkt, int radius, Pageable pageable);
//
//    List<PlaceWithDistance> findNearbyPlacesOrderByName(String pointWkt, int radius, Pageable pageable);
//
//    List<PlaceWithDistance> findNearbyPlacesOrderByLikeCount(String pointWkt, int radius, Pageable pageable);

    Page<PlaceNearbyDto> findNearbyPlaces(double latitude, double longitude, int radius, Long categoryId, Pageable pageable);

    Map<Long, List<String>> findCategoryNamesMapByPlaceIds(List<Long> placeIds);

    void increaseLikeCount(Long aLong);

    void decreaseLikeCount(Long aLong);
}
