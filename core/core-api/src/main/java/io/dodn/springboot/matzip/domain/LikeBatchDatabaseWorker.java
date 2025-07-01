package io.dodn.springboot.matzip.domain;

import io.dodn.springboot.matzip.domain.model.LikeTask;
import io.dodn.springboot.matzip.domain.model.LikeTaskQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class LikeBatchDatabaseWorker {
    private static final Logger log = LoggerFactory.getLogger(LikeBatchDatabaseWorker.class);
    private final LikeTaskQueue likeTaskQueue;
    private final JdbcTemplate jdbcTemplate; // 대규모 Batch 작업에는 JPA보다 JdbcTemplate이 더 효율적

    public LikeBatchDatabaseWorker(final LikeTaskQueue likeTaskQueue, final JdbcTemplate jdbcTemplate) {
        this.likeTaskQueue = likeTaskQueue;
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1.5초마다 큐를 확인하여 작업을 처리
    @Transactional
    @Scheduled(fixedDelay = 1500)
    public void processLikeTasks() {
        // 큐에 쌓인 모든 작업을 가져온다.
        List<LikeTask> tasks = likeTaskQueue.pollTask();

        if (tasks.isEmpty()) {
            return;
        }

        log.info("Processing {} like tasks.", tasks.size());

        // '좋아요'와 '좋아요 취소' 작업을 분리
        List<LikeTask> likeTasks = tasks.stream().filter(LikeTask::isLike).toList();
        List<LikeTask> unlikeTasks = tasks.stream().filter(task -> !task.isLike()).toList();

        // JDBC Batch Update를 사용하여 한 번의 커넥션으로 모든 작업을 처리
        if (!likeTasks.isEmpty()) {
            // INSERT, IGNORE를 사용하여 이미 좋아요를 누른 경우 중복 오류를 방지
            jdbcTemplate.batchUpdate("INSERT IGNORE INTO place_like (member_id, place_id, created_at, updated_at) VALUES (?, ?, NOW(), NOW())",
                    likeTasks,
                    100, // 100개씩 끊어서 실행
                    (PreparedStatement ps, LikeTask task) -> {
                        ps.setLong(1, task.userId());
                        ps.setLong(2, task.placeId());
                    });
            log.info("Batch INSERT for {} likes completed.", likeTasks.size());
        }

        if (!unlikeTasks.isEmpty()) {
            jdbcTemplate.batchUpdate("DELETE FROM place_like WHERE member_id = ? AND place_id = ?",
                    unlikeTasks,
                    100,
                    (PreparedStatement ps, LikeTask task) -> {
                        ps.setLong(1, task.userId());
                        ps.setLong(2, task.placeId());
                    });
            log.info("Batch DELETE for {} unlikes completed.", unlikeTasks.size());
        }
    }
}
