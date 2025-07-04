package io.dodn.springboot.storage.db.event.adapter;

import io.dodn.springboot.storage.db.event.EventRepository;
import io.dodn.springboot.storage.db.event.entity.Event;
import io.dodn.springboot.storage.db.event.repository.EventJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EventRepositoryAdapter implements EventRepository {
    private final EventJpaRepository eventJpaRepository;

    public EventRepositoryAdapter(final EventJpaRepository eventJpaRepository) {
        this.eventJpaRepository = eventJpaRepository;
    }
    @Override
    public List<Event> findEventsForPeriod(final LocalDateTime startDate, final LocalDateTime endDate) {
        return eventJpaRepository.findAllByStartDatetimeBetweenOrderByEventCategoryAsc(startDate, endDate);
    }
}
