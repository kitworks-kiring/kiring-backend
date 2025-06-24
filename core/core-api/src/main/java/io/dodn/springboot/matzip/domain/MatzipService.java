package io.dodn.springboot.matzip.domain;

import io.dodn.springboot.matzip.controller.response.LikeToggleResponse;
import io.dodn.springboot.matzip.controller.response.NearbyPlaceResponse;
import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.dodn.springboot.matzip.exception.NotFoundPlaceException;
import io.dodn.springboot.member.exception.NotFoundMemberException;
import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.dto.PlaceNearbyDto;
import io.dodn.springboot.storage.db.matzip.entity.Category;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.entity.PlaceLike;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatzipService {
    private final MatzipRepository matzipRepository;
    private final MemberRepository memberRepository;

    public MatzipService(final MatzipRepository matzipRepository, final MemberRepository memberRepository) {
        this.matzipRepository = matzipRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public Page<PlaceResponse> findAllPlaces(
            final Long memberId,
            final Pageable pageable
    ) {
        Page<Place> places = matzipRepository.findAllWithCategories(pageable);
        List<Place> placeList = places.getContent();

        if (placeList.isEmpty()) {
            return Page.empty(pageable);
        }
        Set<Long> likedPlaceIds = getLikedPlaceIds(memberId, placeList);

        return places.map(place ->
                PlaceResponse.of(place, likedPlaceIds.contains(place.getId()))
        );
    }

    private Set<Long> getLikedPlaceIds(
            final Long memberId,
            final List<Place> placeList
    ) {
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

    @Transactional
    public LikeToggleResponse toggleLike(
            final Long memberId,
            final Long placeId
    ) {
        // 1. 기존 '좋아요' 기록이 있는지 확인합니다.
        Optional<PlaceLike> existingLike = matzipRepository.findByMemberIdAndPlaceId(memberId, placeId);

        Place place = matzipRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new NotFoundPlaceException("맛집을 찾을 수 없습니다."));

        // 2. '좋아요' 기록이 있다면 -> 좋아요 취소 (삭제 및 카운트 감소)
        if (existingLike.isPresent()) {
            matzipRepository.delete(existingLike.get());
            place.decreaseLikeCount();
            return new LikeToggleResponse(false, place.getLikeCount()); // isLiked: false, 업데이트된 카운트
        }
        // 3. '좋아요' 기록이 없다면 -> 좋아요 처리 (삽입 및 카운트 증가)
        else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundMemberException("사용자를 찾을 수 없습니다."));

            PlaceLike newLike = new PlaceLike(member, place);
            matzipRepository.save(newLike);

            place.increaseLikeCount();
            return new LikeToggleResponse(true, place.getLikeCount()); // isLiked: true, 업데이트된 카운트
        }
    }

    @Transactional(readOnly = true)
    public Page<NearbyPlaceResponse> findNearbyPlaces(
            final double latitude,
            final double longitude,
            final int radius,
            final String categoryName,
            final Pageable pageable,
            final Long memberId
    ) {
        Long categoryId = null;
        if (categoryName != null && !categoryName.isEmpty()) {
            List<String> categoryNames = List.of(categoryName.trim().split(","));
            List<Category> categories = matzipRepository.categoryFindByNameIn(categoryNames);
            if (!categories.isEmpty()) {
                categoryId = categories.getFirst().getId();
            }
        }

        Page<PlaceNearbyDto> dtoPage = matzipRepository.findNearbyPlaces(latitude, longitude, radius, categoryId, pageable);
        if (!dtoPage.hasContent()) {
            return Page.empty(pageable);
        }

        List<PlaceNearbyDto> dos = dtoPage.getContent();
        List<Long> placeIds = dos.stream()
                .map(PlaceNearbyDto::placeId)
                .collect(Collectors.toList());


        final Set<Long> likedPlaceIds = matzipRepository.findLikedPlaceIdsByMemberAndPlaceIds(memberId, placeIds);
        final Map<Long, List<String>> categoriesMap = matzipRepository.findCategoryNamesMapByPlaceIds(placeIds);

        return dtoPage.map(dto -> NearbyPlaceResponse.from(dto, categoriesMap, likedPlaceIds.contains(dto.placeId())));
    }

    @Transactional(readOnly = true)
    public Page<PlaceResponse> findAllPlacesWithNPlusOne(final Long memberId, final Pageable pageable) {
        Page<Place> places = matzipRepository.findAll(pageable);
        List<Place> placeList = places.getContent();

        if (placeList.isEmpty()) {
            return Page.empty(pageable);
        }

        // N+1 문제를 의도적으로 발생시키기 위해 각 Place의 카테고리를 개별적으로 로드합니다.
        for (Place place : placeList) {
            place.getCategories().size(); // 이 호출이 N+1 문제를 발생시킵니다.
        }

        Set<Long> likedPlaceIds = getLikedPlaceIds(memberId, placeList);

        return places.map(place ->
                PlaceResponse.of(place, false)
        );
    }

}
