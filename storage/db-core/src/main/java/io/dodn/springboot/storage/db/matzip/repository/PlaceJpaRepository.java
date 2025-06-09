package io.dodn.springboot.storage.db.matzip.repository;

import io.dodn.springboot.storage.db.matzip.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceJpaRepository extends JpaRepository<Place, Long> {

}
