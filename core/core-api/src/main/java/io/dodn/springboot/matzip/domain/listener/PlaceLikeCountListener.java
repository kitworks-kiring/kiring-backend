package io.dodn.springboot.matzip.domain.listener;

import io.dodn.springboot.matzip.domain.event.LikeToggledEvent;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.repository.PlaceJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PlaceLikeCountListener {
    private static final Logger log = LoggerFactory.getLogger(PlaceLikeCountListener.class);
    private final PlaceJpaRepository placeJpaRepository;

    public PlaceLikeCountListener(final PlaceJpaRepository placeJpaRepository) {
        this.placeJpaRepository = placeJpaRepository;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLikeToggledEvent(LikeToggledEvent event) {
        try {
            Place place = placeJpaRepository.findById(event.placeId())
                    .orElse(null);

            if (place == null) {
                log.warn("Place not found for like count update. placeId: {}", event.placeId());
                return;
            }

            if (event.isLikeAction()) {
                place.increaseLikeCount();
            } else {
                place.decreaseLikeCount();
            }
            log.info("Updated like count for placeId: {}. New count: {}", event.placeId(), place.getLikeCount());
        } catch (Exception e) {
            log.error("Failed to process like count update event for placeId: {}", event.placeId(), e);
        }
    }

}
