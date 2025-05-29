package io.dodn.springboot.storage.db.core.repository;

import io.dodn.springboot.storage.db.core.entity.ExampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleJpaRepository extends JpaRepository<ExampleEntity, Long> {

}
