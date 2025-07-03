package io.dodn.springboot.matzip.domain.listener;

import io.dodn.springboot.matzip.domain.event.LikeToggledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PlaceLikePersistenceListener {
    private static final Logger log = LoggerFactory.getLogger(PlaceLikePersistenceListener.class);
    private final JdbcTemplate jdbcTemplate;

    public PlaceLikePersistenceListener(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 메인 트랜잭션과 분리
    public void handleLikeToggledEvent(LikeToggledEvent event) {
        try {
            if (event.isLikeAction()) {
                // '좋아요'를 한 경우: INSERT IGNORE를 사용하여 중복 오류 방지
                jdbcTemplate.update("INSERT IGNORE INTO place_like (member_id, place_id, created_at, updated_at) VALUES (?, ?, NOW(), NOW())",
                        event.memberId(), event.placeId());
                log.info("Processed a LIKE action for userId: {}, placeId: {}", event.memberId(), event.placeId());
            } else {
                // '좋아요 취소'를 한 경우: DELETE
                jdbcTemplate.update("DELETE FROM place_like WHERE member_id = ? AND place_id = ?",
                        event.memberId(), event.placeId());
                log.info("Processed an UNLIKE action for userId: {}, placeId: {}", event.memberId(), event.placeId());
            }
        } catch (Exception e) {
            log.error("Failed to process like persistence event for userId: {}, placeId: {}", event.memberId(), event.placeId(), e);
        }
    }
}
