package io.dodn.springboot.matzip.domain.listener;

import io.dodn.springboot.matzip.domain.event.PlaceLikeCancelledEvent;
import io.dodn.springboot.matzip.domain.event.PlaceLikedEvent;
import io.dodn.springboot.matzip.exception.NotFoundPlaceException;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.repository.PlaceJpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PlaceLikeCountListener {
    private final PlaceJpaRepository placeJpaRepository;

    public PlaceLikeCountListener(final PlaceJpaRepository placeJpaRepository) {
        this.placeJpaRepository = placeJpaRepository;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 트랜잭션을 분리하여 이벤트 발행 측의 트랜잭션과 독립적으로 실행
    public void handlePlaceLikedEvent(PlaceLikedEvent event) {
        Place place = placeJpaRepository.findById(event.placeId())
                .orElseThrow(() -> new NotFoundPlaceException("맛집을 찾을 수 없습니다."));
        place.increaseLikeCount();
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handlePlaceLikeCancelledEvent(PlaceLikeCancelledEvent event) {
        Place place = placeJpaRepository.findById(event.placeId())
                .orElseThrow(() -> new NotFoundPlaceException("맛집을 찾을 수 없습니다."));
        place.decreaseLikeCount();
    }

}
