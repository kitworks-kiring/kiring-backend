package io.dodn.springboot.core.domain;

import io.dodn.springboot.storage.db.core.ExampleRepository;
import io.dodn.springboot.storage.db.core.entity.ExampleEntity;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    private final ExampleRepository exampleRepository;

    public ExampleService(final ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    public ExampleResult processExample(ExampleData exampleData) {
        final ExampleEntity entity = exampleRepository.findById(1L)
            .orElseThrow(() -> new IllegalArgumentException("Example not found with id: " + exampleData.value()));
        return new ExampleResult(exampleData.value());
    }

}
