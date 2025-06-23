package io.dodn.springboot.storage.db.plane.repository;

import io.dodn.springboot.storage.db.member.entity.Member;
import io.dodn.springboot.storage.db.plane.entity.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PlaneJpaRepository extends JpaRepository<Plane, Long>{
    List<Plane> findAllByReceiverIdOrderByCreatedAtDesc(long readerId);

    boolean existsBySenderAndCreatedAtBetween(Member sender, LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Plane> findByReceiverIdAndCreatedAtBetween(long readerId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    long countBySenderIdAndCreatedAtBetween(Long readerId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
