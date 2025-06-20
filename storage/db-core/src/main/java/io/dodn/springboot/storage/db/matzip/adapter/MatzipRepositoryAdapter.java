package io.dodn.springboot.storage.db.matzip.adapter;

import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.dto.PlaceNearbyDto;
import io.dodn.springboot.storage.db.matzip.entity.Category;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.entity.PlaceLike;
import io.dodn.springboot.storage.db.matzip.repository.CategoryJpaRepository;
import io.dodn.springboot.storage.db.matzip.repository.MenuJpaRepository;
import io.dodn.springboot.storage.db.matzip.repository.PlaceJpaRepository;
import io.dodn.springboot.storage.db.matzip.repository.PlaceLikeJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class MatzipRepositoryAdapter implements MatzipRepository {
    private final MenuJpaRepository menuJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceLikeJpaRepository placeLikeJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public MatzipRepositoryAdapter(final MenuJpaRepository menuJpaRepository, final PlaceJpaRepository placeJpaRepository, final PlaceLikeJpaRepository placeLikeJpaRepository, final CategoryJpaRepository categoryJpaRepository) {
        this.menuJpaRepository = menuJpaRepository;
        this.placeJpaRepository = placeJpaRepository;
        this.placeLikeJpaRepository = placeLikeJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }


    @Override
    public Page<Place> findAll(final Pageable pageable) {
        return placeJpaRepository.findAll(pageable);
    }

    @Override
    public Set<Long> findLikedPlaceIdsByMemberAndPlaceIds(final Long memberId, final List<Long> placeIds) {
        return placeLikeJpaRepository.findLikedPlaceIdsByMemberAndPlaceIds(memberId, placeIds);
    }

    @Override
    public Optional<PlaceLike> findByMemberIdAndPlaceId(final Long memberId, final Long placeId) {
        return placeLikeJpaRepository.findByMemberIdAndPlaceId(memberId, placeId);

    }

    @Override
    public Optional<Place> findByPlaceId(final Long placeId) {
        return placeJpaRepository.findById(placeId);
    }

    @Override
    public void delete(final PlaceLike placeLike) {
        placeLikeJpaRepository.delete(placeLike);
    }

    @Override
    public void save(final PlaceLike newLike) {
        placeLikeJpaRepository.save(newLike);
    }

    @Override
    public long countNearbyPlaces(final String pointWkt, final int radius) {
        return placeJpaRepository.countNearbyPlaces(pointWkt, radius);
    }

    @Override
    public void saveAll(final List<Place> placesToSave) {
        placeJpaRepository.saveAll(placesToSave);
    }

    @Override
    public Category saveCategory(final Category newCategory) {
        return categoryJpaRepository.save(newCategory);
    }

    @Override
    public List<Category> categoryFindByNameIn(final List<String> categoryNames) {
        return categoryJpaRepository.findByNameIn(categoryNames);
    }

    @Override
    public Page<Place> findAllWithCategories(final Pageable pageable) {
        return placeJpaRepository.findAllWithCategories(pageable);
    }

//    @Override
//    public List<PlaceWithDistance> findNearbyPlacesOrderByDistance(final String pointWkt, final int radius, final Pageable pageable) {
//        return placeJpaRepository.findNearbyPlacesOrderByDistance(pointWkt, radius, pageable);
//    }
//
//    @Override
//    public List<PlaceWithDistance> findNearbyPlacesOrderByName(final String pointWkt, final int radius, final Pageable pageable) {
//        return placeJpaRepository.findNearbyPlacesOrderByName(pointWkt, radius, pageable);
//    }
//
//    @Override
//    public List<PlaceWithDistance> findNearbyPlacesOrderByLikeCount(final String pointWkt, final int radius, final Pageable pageable) {
//        return placeJpaRepository.findNearbyPlacesOrderByLikeCount(pointWkt, radius, pageable);
//    }

    @Override
    public Page<PlaceNearbyDto> findNearbyPlaces(final double latitude, final double longitude, final int radius, final Long categoryId, final Pageable pageable) {
        return placeJpaRepository.findNearbyPlaces(latitude, longitude, radius, categoryId, pageable);
    }

    @Override
    public Map<Long, List<String>> findCategoryNamesMapByPlaceIds(final List<Long> placeIds) {
        return placeJpaRepository.findCategoryNamesMapByPlaceIds(placeIds);
    }


}
