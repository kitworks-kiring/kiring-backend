package io.dodn.springboot.storage.db.plane;

import io.dodn.springboot.storage.db.plane.entity.Plane;

import java.util.List;

public interface PlaneRepository {
    Plane save(Plane plane);

    List<Plane> findByReceiverId(long readerId);
}
