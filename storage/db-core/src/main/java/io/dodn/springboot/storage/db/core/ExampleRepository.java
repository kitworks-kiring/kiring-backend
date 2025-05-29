package io.dodn.springboot.storage.db.core;

import io.dodn.springboot.storage.db.core.entity.ExampleEntity;

import java.util.Optional;

public interface ExampleRepository {

    /**
 * 주어진 ID에 해당하는 ExampleEntity를 조회합니다.
 *
 * @param id 조회할 엔티티의 고유 식별자
 * @return 해당 ID의 ExampleEntity가 존재하면 Optional에 담아 반환하며, 없으면 빈 Optional을 반환합니다.
 */
Optional<ExampleEntity> findById(Long id);

    /**
 * 지정된 ID를 가진 엔티티의 exampleColumn 값을 업데이트합니다.
 *
 * @param id 업데이트할 엔티티의 ID
 * @param exampleColumn 새로 설정할 exampleColumn 값
 * @return 업데이트된 행의 수
 */
int updateExampleColumn(Long id, String exampleColumn);

}
