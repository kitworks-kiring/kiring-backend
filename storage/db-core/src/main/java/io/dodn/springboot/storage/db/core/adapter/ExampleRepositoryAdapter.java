package io.dodn.springboot.storage.db.core.adapter;

import io.dodn.springboot.storage.db.core.ExampleRepository;
import io.dodn.springboot.storage.db.core.entity.ExampleEntity;
import io.dodn.springboot.storage.db.core.repository.ExampleJpaRepository;
import io.dodn.springboot.storage.db.core.repository.ExampleMybatisRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ExampleRepositoryAdapter implements ExampleRepository {

    private final ExampleJpaRepository exampleJpaRepository;

    private final ExampleMybatisRepository exampleMybatisRepository;

    public ExampleRepositoryAdapter(final ExampleJpaRepository exampleJpaRepository,
            final ExampleMybatisRepository exampleMybatisRepository) {
        this.exampleJpaRepository = exampleJpaRepository;
        this.exampleMybatisRepository = exampleMybatisRepository;
    }

    @Override
    public Optional<ExampleEntity> findById(final Long id) {
        return exampleJpaRepository.findById(id);
    }

    @Override
    public int updateExampleColumn(final Long id, final String exampleColumn) {
        return exampleMybatisRepository.updateExampleColumn(id, exampleColumn);
    }

}
