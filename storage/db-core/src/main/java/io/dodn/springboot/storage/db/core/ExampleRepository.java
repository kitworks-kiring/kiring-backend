package io.dodn.springboot.storage.db.core;

import io.dodn.springboot.storage.db.core.entity.ExampleEntity;

import java.util.Optional;

public interface ExampleRepository {
    Optional<ExampleEntity> findById(Long id);

    int updateExampleColumn(Long id, String exampleColumn);
}
