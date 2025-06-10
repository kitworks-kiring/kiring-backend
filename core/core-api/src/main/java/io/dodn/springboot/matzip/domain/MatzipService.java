package io.dodn.springboot.matzip.domain;

import io.dodn.springboot.matzip.controller.response.LikeToggleResponse;
import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.dodn.springboot.matzip.exception.NotFoundPlaceException;
import io.dodn.springboot.member.exception.NotFoundMemberException;
import io.dodn.springboot.storage.db.matzip.MatzipRepository;
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
        return places.map(place ->
                PlaceResponse.of(place, likedPlaceIds.contains(place.getId()))
        );
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

    @Transactional
    public LikeToggleResponse toggleLike(final Long memberId, final Long placeId) {
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
}
