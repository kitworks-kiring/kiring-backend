package io.dodn.springboot.core.domain;

import io.dodn.springboot.storage.db.core.ExampleRepository;
import io.dodn.springboot.storage.db.core.entity.ExampleEntity;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    private final ExampleRepository exampleRepository;

    /**
     * ExampleService의 인스턴스를 생성하고 ExampleRepository를 주입합니다.
     *
     * @param exampleRepository 예제 데이터 처리를 위한 리포지토리
     */
    public ExampleService(final ExampleRepository exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    /**
     * 입력된 예제 데이터를 처리하고, 데이터베이스에서 예제 엔티티가 존재하는지 확인한 후 결과를 반환합니다.
     *
     * @param exampleData 처리할 예제 데이터
     * @return 입력 데이터의 값을 기반으로 생성된 ExampleResult 객체
     * @throws IllegalArgumentException 예제 엔티티가 데이터베이스에 존재하지 않을 경우 발생
     */
    public ExampleResult processExample(ExampleData exampleData) {
        final ExampleEntity entity = exampleRepository.findById(1L)
            .orElseThrow(() -> new IllegalArgumentException("Example not found with id: " + exampleData.value()));
        return new ExampleResult(exampleData.value());
    }

}
