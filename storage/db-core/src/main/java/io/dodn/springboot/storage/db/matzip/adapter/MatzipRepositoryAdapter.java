package io.dodn.springboot.storage.db.matzip.adapter;

import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.entity.PlaceLike;
import io.dodn.springboot.storage.db.matzip.repository.MenuJpaRepository;
import io.dodn.springboot.storage.db.matzip.repository.PlaceJpaRepository;
import io.dodn.springboot.storage.db.matzip.repository.PlaceLikeJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class MatzipRepositoryAdapter implements MatzipRepository {
    private final MenuJpaRepository menuJpaRepository;
    private final PlaceJpaRepository placeJpaRepository;
    private final PlaceLikeJpaRepository placeLikeJpaRepository;

    public MatzipRepositoryAdapter(final MenuJpaRepository menuJpaRepository, final PlaceJpaRepository placeJpaRepository, final PlaceLikeJpaRepository placeLikeJpaRepository) {
        this.menuJpaRepository = menuJpaRepository;
        this.placeJpaRepository = placeJpaRepository;
        this.placeLikeJpaRepository = placeLikeJpaRepository;
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


}
