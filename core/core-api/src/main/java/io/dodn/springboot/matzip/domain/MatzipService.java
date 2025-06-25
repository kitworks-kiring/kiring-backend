package io.dodn.springboot.matzip.domain;

import io.dodn.springboot.matzip.controller.response.LikeToggleResponse;
import io.dodn.springboot.matzip.controller.response.NearbyPlaceResponse;
import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.dodn.springboot.matzip.domain.event.PlaceLikeCancelledEvent;
import io.dodn.springboot.matzip.domain.event.PlaceLikedEvent;
import io.dodn.springboot.matzip.exception.NotFoundPlaceException;
import io.dodn.springboot.member.exception.NotFoundMemberException;
import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.dto.PlaceNearbyDto;
import io.dodn.springboot.storage.db.matzip.entity.Category;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.entity.PlaceLike;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatzipService {
    private final MatzipRepository matzipRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher; // 이벤트 발행기 주입


    public MatzipService(final MatzipRepository matzipRepository, final MemberRepository memberRepository, final ApplicationEventPublisher eventPublisher) {
        this.matzipRepository = matzipRepository;
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
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
        Place place = matzipRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new NotFoundPlaceException("맛집을 찾을 수 없습니다."));

        // isLiked 상태와 현재 likeCount 를 미리 조회
        boolean isLiked = matzipRepository.findByMemberIdAndPlaceId(memberId, placeId).isPresent();
        long currentLikeCount = place.getLikeCount();

        if (isLiked) {
            // 좋아요 취소 로직
            PlaceLike existingLike = matzipRepository.findByMemberIdAndPlaceId(memberId, placeId).get();
            matzipRepository.delete(existingLike);
            // 이벤트 발행 (카운트 감소는 리스너가 담당)
            eventPublisher.publishEvent(new PlaceLikeCancelledEvent(placeId));
            return new LikeToggleResponse(false, currentLikeCount - 1);

        } else {
            // 좋아요 추가 로직
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundMemberException("사용자를 찾을 수 없습니다."));
            matzipRepository.save(new PlaceLike(member, place));

            // 이벤트 발행 (카운트 증가는 리스너가 담당)
            eventPublisher.publishEvent(new PlaceLikedEvent(placeId));
            return new LikeToggleResponse(true, currentLikeCount + 1);
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
