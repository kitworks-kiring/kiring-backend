package io.dodn.springboot.storage.db.plane;

import io.dodn.springboot.storage.db.member.entity.Member;
import io.dodn.springboot.storage.db.plane.entity.Plane;

import java.time.LocalDateTime;
import java.util.List;

public interface PlaneRepository {
    Plane save(Plane plane);

    List<Plane> findByReceiverId(long readerId);

    boolean existsBySenderAndCreatedAtBetween(Member sender, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Plane> findByReceiverIdAndCreatedAtBetween(long readerId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
