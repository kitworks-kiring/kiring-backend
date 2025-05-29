package io.dodn.springboot.storage.db.core.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExampleMybatisRepository {

    /****
 * 지정된 ID를 가진 레코드의 exampleColumn 값을 업데이트합니다.
 *
 * @param id 업데이트할 레코드의 고유 식별자
 * @param exampleColumn 새로 설정할 exampleColumn 값
 * @return 업데이트된 행의 수
 */
int updateExampleColumn(Long id, String exampleColumn);

}
