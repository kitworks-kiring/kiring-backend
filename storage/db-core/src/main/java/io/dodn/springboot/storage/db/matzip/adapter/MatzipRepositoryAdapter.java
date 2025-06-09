package io.dodn.springboot.storage.db.matzip.adapter;

import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.repository.MenuJpaRepository;
import io.dodn.springboot.storage.db.matzip.repository.PlaceJpaRepository;
import io.dodn.springboot.storage.db.matzip.repository.PlaceLikeJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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
}
