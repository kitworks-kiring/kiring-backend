package io.dodn.springboot.matzip;

import io.dodn.springboot.matzip.controller.response.LikeToggleResponse;
import io.dodn.springboot.matzip.controller.response.PlaceResponse;
import io.dodn.springboot.matzip.domain.MatzipService;
import io.dodn.springboot.matzip.domain.event.PlaceLikeCancelledEvent;
import io.dodn.springboot.matzip.domain.event.PlaceLikedEvent;
import io.dodn.springboot.matzip.exception.NotFoundPlaceException;
import io.dodn.springboot.member.exception.NotFoundMemberException;
import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.entity.PlaceLike;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.dodn.springboot.helper.EntityFactory.createPlace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MatzipServiceTest {

    @InjectMocks
    private MatzipService matzipService;

    @Mock
    private MatzipRepository matzipRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;


    @Mock
    private Place place; // Mock Place for count verification

    @DisplayName("맛집 전체 목록을 성공적으로 조회한다.")
    @Test
    void findAllPlaces_success() {
        // given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        // ✅ new Place() 대신 팩토리를 사용하여 ID가 있는 객체 생성
        Place place1 = createPlace(1L, "맛집1");
        Place place2 = createPlace(2L, "맛집2");

        List<Place> places = List.of(place1, place2);
        Page<Place> placePage = new PageImpl<>(places, pageable, 2);

        // place.getId()가 정상적으로 1L, 2L을 반환하게 됨
        List<Long> placeIds = places.stream().map(Place::getId).toList(); // [1L, 2L]

        given(matzipRepository.findAllWithCategories(pageable)).willReturn(placePage);
        given(matzipRepository.findLikedPlaceIdsByMemberAndPlaceIds(anyLong(), eq(placeIds))).willReturn(Set.of(2L)); // 맛집2는 좋아요 누른 상태로 가정

        // when
        Page<PlaceResponse> result = matzipService.findAllPlaces(memberId, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).isLiked()).isFalse(); // 맛집1
        assertThat(result.getContent().get(1).isLiked()).isTrue();  // 맛집2
    }

//    @DisplayName("좋아요를 처음 누르면, 좋아요가 추가되고 카운트가 증가한다.")
//    @Test
//    void toggleLike_addLike_success() {
//        // given
//        Long memberId = 1L;
//        Long placeId = 1L;
//        Member member = new Member("테스터", "test@test.com", "010-1234-5678", null, null, null, null, null, null, false, false, null, null);
//
//        given(matzipRepository.findByMemberIdAndPlaceId(memberId, placeId)).willReturn(Optional.empty());
//        given(matzipRepository.findByPlaceId(placeId)).willReturn(Optional.of(place));
//        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
//        given(place.getLikeCount()).willReturn(1); // increaseLikeCount 후의 값
//
//        // when
//        LikeToggleResponse response = matzipService.toggleLike(memberId, placeId);
//
//        // then
//        assertThat(response.isLiked()).isTrue();
//        assertThat(response.likeCount()).isEqualTo(1);
//        verify(matzipRepository).save(any(PlaceLike.class));
//        verify(place).increaseLikeCount();
//        verify(place, never()).decreaseLikeCount();
//    }
//
//    @DisplayName("이미 좋아요를 눌렀으면, 좋아요가 취소되고 카운트가 감소한다.")
//    @Test
//    void toggleLike_cancelLike_success() {
//        // given
//        Long memberId = 1L;
//        Long placeId = 1L;
//        PlaceLike existingLike = new PlaceLike(null, place);
//
//        given(matzipRepository.findByMemberIdAndPlaceId(memberId, placeId)).willReturn(Optional.of(existingLike));
//        given(matzipRepository.findByPlaceId(placeId)).willReturn(Optional.of(place));
//        given(place.getLikeCount()).willReturn(0); // decreaseLikeCount 후의 값
//
//        // when
//        LikeToggleResponse response = matzipService.toggleLike(memberId, placeId);
//
//        // then
//        assertThat(response.isLiked()).isFalse();
//        assertThat(response.likeCount()).isEqualTo(0);
//        verify(matzipRepository).delete(any(PlaceLike.class));
//        verify(place).decreaseLikeCount();
//        verify(place, never()).increaseLikeCount();
//    }

    @DisplayName("좋아요를 처음 누르면, DB에 저장하고 PlaceLikedEvent를 발행한다.")
    @Test
    void toggleLike_shouldAddLikeAndPublishEvent_whenNotLiked() {
        // given
        Long memberId = 1L;
        Long placeId = 10L;
        Member mockMember = new Member("테스터", "test@test.com", "010-1234-5678", null, null, null, null, null, null, false, false, null, null);
        

        given(matzipRepository.findByPlaceId(placeId)).willReturn(Optional.of(place));
        given(matzipRepository.findByMemberIdAndPlaceId(memberId, placeId)).willReturn(Optional.empty()); // 처음이므로 '좋아요' 기록 없음
        given(memberRepository.findById(memberId)).willReturn(Optional.of(mockMember));
        given(place.getLikeCount()).willReturn(5); // 현재 좋아요 수 5개

        // when
        LikeToggleResponse response = matzipService.toggleLike(memberId, placeId);

        // then
        // 1. 응답 검증: isLiked는 true, likeCount는 현재 카운트+1
        assertThat(response.isLiked()).isTrue();
        assertThat(response.likeCount()).isEqualTo(6);

        // 2. DB 저장 로직 호출 검증
        verify(matzipRepository).save(any(PlaceLike.class));

        // 3. 이벤트 발행 검증
        ArgumentCaptor<PlaceLikedEvent> eventCaptor = ArgumentCaptor.forClass(PlaceLikedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture()); // PlaceLikedEvent 발행 검증
        verify(eventPublisher, never()).publishEvent(any(PlaceLikeCancelledEvent.class)); // 취소 이벤트는 발행되지 않았는지 검증

        // 4. 발행된 이벤트 내용 검증 (선택사항이지만 더 견고한 테스트)
        PlaceLikedEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.placeId()).isEqualTo(placeId);
    }

    @DisplayName("이미 좋아요를 눌렀으면, DB에서 삭제하고 PlaceLikeCancelledEvent를 발행한다.")
    @Test
    void toggleLike_shouldCancelLikeAndPublishEvent_whenAlreadyLiked() {
        // given
        Long memberId = 1L;
        Long placeId = 10L;
        PlaceLike mockPlaceLike = new PlaceLike(null, place);

        given(matzipRepository.findByPlaceId(placeId)).willReturn(Optional.of(place));
        // 이미 좋아요를 누른 상태이므로 Optional.of(mockPlaceLike)를 반환
        given(matzipRepository.findByMemberIdAndPlaceId(memberId, placeId)).willReturn(Optional.of(mockPlaceLike));
        given(place.getLikeCount()).willReturn(5); // 현재 좋아요 수 5개

        // when
        LikeToggleResponse response = matzipService.toggleLike(memberId, placeId);

        // then
        // 1. 응답 검증: isLiked는 false, likeCount는 현재 카운트-1
        assertThat(response.isLiked()).isFalse();
        assertThat(response.likeCount()).isEqualTo(4);

        // 2. DB 삭제 로직 호출 검증
        verify(matzipRepository).delete(mockPlaceLike);

        // 3. 이벤트 발행 검증
        ArgumentCaptor<PlaceLikeCancelledEvent> eventCaptor = ArgumentCaptor.forClass(PlaceLikeCancelledEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture()); // PlaceLikeCancelledEvent 발행 검증
        verify(eventPublisher, never()).publishEvent(any(PlaceLikedEvent.class)); // 추가 이벤트는 발행되지 않았는지 검증

        // 4. 발행된 이벤트 내용 검증
        PlaceLikeCancelledEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.placeId()).isEqualTo(placeId);
    }

    @DisplayName("존재하지 않는 맛집에 좋아요를 누르면 예외가 발생한다.")
    @Test
    void toggleLike_placeNotFound_throwsException() {
        // given
        Long memberId = 1L;
        Long nonExistPlaceId = 999L;
        given(matzipRepository.findByPlaceId(nonExistPlaceId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> matzipService.toggleLike(memberId, nonExistPlaceId))
                .isInstanceOf(NotFoundPlaceException.class)
                .hasMessage("맛집을 찾을 수 없습니다.");
    }

    @DisplayName("존재하지 않는 사용자가 좋아요를 누르면 예외가 발생한다.")
    @Test
    void toggleLike_memberNotFound_throwsException() {
        // given
        Long nonExistMemberId = 999L;
        Long placeId = 1L;

        given(matzipRepository.findByMemberIdAndPlaceId(nonExistMemberId, placeId)).willReturn(Optional.empty());
        given(matzipRepository.findByPlaceId(placeId)).willReturn(Optional.of(place));
        given(memberRepository.findById(nonExistMemberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> matzipService.toggleLike(nonExistMemberId, placeId))
                .isInstanceOf(NotFoundMemberException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }
}