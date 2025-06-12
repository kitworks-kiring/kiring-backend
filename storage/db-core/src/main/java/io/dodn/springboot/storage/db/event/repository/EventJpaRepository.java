package io.dodn.springboot.storage.db.event.repository;

import io.dodn.springboot.storage.db.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventJpaRepository extends JpaRepository<Event, Long> {

}
