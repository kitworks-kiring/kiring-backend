package io.dodn.springboot.storage.db.core.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExampleMybatisRepository {
    int updateExampleColumn(Long id, String exampleColumn);
}
