package io.dodn.springboot.storage.db.matzip.repository;

import io.dodn.springboot.storage.db.matzip.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {
}
