package io.dodn.springboot.matzip.domain;

import io.dodn.springboot.matzip.controller.response.LikeToggleResponse;
import io.dodn.springboot.matzip.controller.response.NearbyPlaceResponse;
import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.dodn.springboot.matzip.domain.event.LikeToggledEvent;
import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.dto.PlaceNearbyDto;
import io.dodn.springboot.storage.db.matzip.entity.Category;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.member.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
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
//    private final LikeTaskQueue likeTaskQueue;   스프링 표준 이벤트 발행으로 통일하기로 (커스텀 비동기 방식 x)
    private final StringRedisTemplate redisTemplate; // ✅ StringRedisTemplate 주입

    public MatzipService(final MatzipRepository matzipRepository, final MemberRepository memberRepository, final ApplicationEventPublisher eventPublisher, final StringRedisTemplate redisTemplate) {
        this.matzipRepository = matzipRepository;
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
        this.redisTemplate = redisTemplate;
    }

    private String getLikeSetKey(Long placeId) {
        return "place:likes:" + placeId;
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
        final String likeSetKey = getLikeSetKey(placeId);
        final String memberIdStr = String.valueOf(memberId);

        // 1. Redis에서 현재 좋아요 상태 확인
        boolean isCurrentlyLiked = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeSetKey, memberIdStr));

        boolean isLikedAction; // 이번 액션의 결과 (true: 좋아요 됨, false: 좋아요 취소됨)

        // 2. Redis에 좋아요 상태 변경 (빠른 응답을 위해 먼저 처리)
        if (!isCurrentlyLiked) {
            // 좋아요를 누른다 (Redis에 추가)
            redisTemplate.opsForSet().add(likeSetKey, memberIdStr);
            isLikedAction = true;
        } else {
            // 좋아요를 취소한다 (Redis에서 제거)
            redisTemplate.opsForSet().remove(likeSetKey, memberIdStr);
            isLikedAction = false;
        }

        // 3. 비동기 이벤트 발행 (DB 업데이트는 백그라운드에서 처리)
        eventPublisher.publishEvent(new LikeToggledEvent(memberId, placeId, isLikedAction));

        // 4. 사용자에게 즉시 응답 반환 (Redis 변경 결과를 기반)
        return new LikeToggleResponse(isLikedAction);
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
