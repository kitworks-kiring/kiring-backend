package io.dodn.springboot.storage.db.plane.repository;

import io.dodn.springboot.storage.db.plane.entity.Plane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaneJpaRepository extends JpaRepository<Plane, Long>{
    List<Plane> findAllByReceiverId(long readerId);
}
