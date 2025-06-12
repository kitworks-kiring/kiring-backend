package io.dodn.springboot.storage.db.event;

import io.dodn.springboot.storage.db.event.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository {
    List<Event> findEventsForPeriod(LocalDateTime startDate, LocalDateTime endDate);
}
