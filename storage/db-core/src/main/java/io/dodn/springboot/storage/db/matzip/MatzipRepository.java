package io.dodn.springboot.storage.db.matzip;

import io.dodn.springboot.storage.db.matzip.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface MatzipRepository {
    Page<Place> findAll(Pageable pageable);

    Set<Long> findLikedPlaceIdsByMemberAndPlaceIds(Long memberId, List<Long> placeIds);
}
