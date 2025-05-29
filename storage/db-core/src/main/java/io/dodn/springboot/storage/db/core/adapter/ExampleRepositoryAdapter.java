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

    /**
     * ExampleRepositoryAdapter의 인스턴스를 생성하며, JPA 및 MyBatis 저장소 구현체를 주입받아 초기화합니다.
     *
     * @param exampleJpaRepository JPA 기반 저장소 구현체
     * @param exampleMybatisRepository MyBatis 기반 저장소 구현체
     */
    public ExampleRepositoryAdapter(final ExampleJpaRepository exampleJpaRepository,
            final ExampleMybatisRepository exampleMybatisRepository) {
        this.exampleJpaRepository = exampleJpaRepository;
        this.exampleMybatisRepository = exampleMybatisRepository;
    }

    /**
     * 지정된 ID에 해당하는 ExampleEntity를 조회합니다.
     *
     * @param id 조회할 엔티티의 ID
     * @return ExampleEntity가 존재하면 Optional에 담아 반환하며, 없으면 빈 Optional을 반환합니다.
     */
    @Override
    public Optional<ExampleEntity> findById(final Long id) {
        return exampleJpaRepository.findById(id);
    }

    /**
     * 지정한 ID의 엔티티에서 exampleColumn 값을 업데이트합니다.
     *
     * @param id 업데이트할 엔티티의 ID
     * @param exampleColumn 새로 설정할 exampleColumn 값
     * @return 업데이트된 행의 수
     */
    @Override
    public int updateExampleColumn(final Long id, final String exampleColumn) {
        return exampleMybatisRepository.updateExampleColumn(id, exampleColumn);
    }

}
