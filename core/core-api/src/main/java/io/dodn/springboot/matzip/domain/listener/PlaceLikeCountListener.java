package io.dodn.springboot.matzip.domain.listener;

import io.dodn.springboot.matzip.domain.event.LikeToggledEvent;
import io.dodn.springboot.storage.db.matzip.MatzipRepository;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import io.dodn.springboot.storage.db.matzip.entity.PlaceLike;
import io.dodn.springboot.storage.db.matzip.repository.PlaceJpaRepository;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PlaceLikeCountListener {
    private static final Logger log = LoggerFactory.getLogger(PlaceLikeCountListener.class);
    private MatzipRepository matzipRepository;
    private MemberRepository memberRepository;

    public PlaceLikeCountListener(final PlaceJpaRepository placeJpaRepository) {
        this.placeJpaRepository = placeJpaRepository;
    }

    @Async
//    @TransactionalEventListener    -> 메인 비지니스 트랜잭션이 커밋되거나 롤백된경우에만 작업이 필요한경우에 사용하는것
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLikeToggledEvent(LikeToggledEvent event) {
        try {
            // Member와 Place 엔티티 조회
            // 이 부분이 추가적인 DB 조회입니다.
            Member member = memberRepository.findById(event.memberId())
                    .orElseThrow(() -> new IllegalArgumentException("Member not found for id: " + event.memberId()));
            Place place = matzipRepository.findByPlaceId(event.placeId())
                    .orElseThrow(() -> new IllegalArgumentException("Place not found for id: " + event.placeId()));

            if (event.isLikeAction()) {
                Optional<PlaceLike> existingLike = matzipRepository.findByMemberIdAndPlaceId(member.getId(), place.getId());

                if (existingLike.isEmpty()) {
                    matzipRepository.save(new PlaceLike(member, place)); // member와 place 엔티티 전달
                    matzipRepository.increaseLikeCount(event.placeId());
                    log.info("Processed a LIKE action for memberId: {}, placeId: {}. Like count increased.", event.memberId(), event.placeId());
                } else {
                    log.info("Member {} already liked place {}. No action taken (INSERT IGNORE equivalent).", event.memberId(), event.placeId());
                }
            } else {
                matzipRepository.findByMemberIdAndPlaceId(member.getId(), place.getId()).ifPresent(placeLike -> {
                    matzipRepository.delete(placeLike);
                    matzipRepository.decreaseLikeCount(event.placeId());
                    log.info("Processed an UNLIKE action for memberId: {}, placeId: {}. Like count decreased.", event.memberId(), event.placeId());
                });
            }
        } catch (Exception e) {
            log.error("Failed to process like persistence event for memberId: {}, placeId: {}. Error: {}",
                    event.memberId(), event.placeId(), e.getMessage(), e);
        }
    }

}
