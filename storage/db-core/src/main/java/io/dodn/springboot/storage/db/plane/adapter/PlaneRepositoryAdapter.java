package io.dodn.springboot.storage.db.plane.adapter;

import io.dodn.springboot.storage.db.member.repository.MemberJpaRepository;
import io.dodn.springboot.storage.db.plane.PlaneRepository;
import io.dodn.springboot.storage.db.plane.entity.Plane;
import io.dodn.springboot.storage.db.plane.repository.PlaneJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlaneRepositoryAdapter implements PlaneRepository {
    private final PlaneJpaRepository planeJpaRepository;
    private final MemberJpaRepository memberJpaRepository;

    public PlaneRepositoryAdapter(PlaneJpaRepository planeJpaRepository, MemberJpaRepository memberJpaRepository) {
        this.planeJpaRepository = planeJpaRepository;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Plane save(final Plane plane) {
        return planeJpaRepository.save(plane);
    }

    @Override
    public List<Plane> findByReceiverId(final long readerId) {
        return planeJpaRepository.findAllByReceiverId(readerId);
    }
}
