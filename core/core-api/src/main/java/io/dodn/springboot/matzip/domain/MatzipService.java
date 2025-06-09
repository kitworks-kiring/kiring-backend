package io.dodn.springboot.matzip.domain;

import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatzipService {
    private final MatzipRepository matzipRepository;

    public MatzipService(final MatzipRepository matzipRepository) {
        this.matzipRepository = matzipRepository;
    }

    @Transactional(readOnly = true)
    public Page<PlaceResponse> findAllPlaces(final Long memberId, final Pageable pageable) {
        // 1. 맛집 목록을 페이지네이션하여 조회합니다.
        Page<Place> places = matzipRepository.findAll(pageable);
        List<Place> placeList = places.getContent();

        if (placeList.isEmpty()) {
            return Page.empty(pageable);
        }

        // 2. 현재 로그인한 사용자의 좋아요 상태를 확인합니다.
        Set<Long> likedPlaceIds = getLikedPlaceIds(memberId, placeList);

        // 3. Place 엔티티를 PlaceResponse DTO로 변환하면서, 좋아요 상태를 설정합니다.
        return places.map(place -> {
            return PlaceResponse.of(place, likedPlaceIds.contains(place.getId()));
        });
    }

    private Set<Long> getLikedPlaceIds(final Long memberId, final List<Place> placeList) {
        // 비로그인 사용자의 경우, 빈 Set을 반환합니다.
        if (memberId == null) {
            return Collections.emptySet();
        }

        // 조회된 맛집들의 ID 목록을 추출합니다.
        List<Long> placeIds = placeList.stream()
                .map(Place::getId)
                .collect(Collectors.toList());

        // 한 번의 쿼리로 현재 사용자가 좋아요를 누른 맛집 ID 목록을 가져옵니다.
        return matzipRepository.findLikedPlaceIdsByMemberAndPlaceIds(memberId, placeIds);
    }
}
