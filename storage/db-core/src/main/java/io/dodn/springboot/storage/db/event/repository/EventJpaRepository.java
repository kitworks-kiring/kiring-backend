package io.dodn.springboot.storage.db.event.repository;

import io.dodn.springboot.storage.db.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventJpaRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByStartDatetimeBetweenOrderByEventCategoryAsc(LocalDateTime startDate, LocalDateTime endDate);
}
