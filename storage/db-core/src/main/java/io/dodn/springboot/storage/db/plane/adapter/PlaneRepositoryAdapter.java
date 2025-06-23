package io.dodn.springboot.storage.db.plane.adapter;

import io.dodn.springboot.storage.db.member.entity.Member;
import io.dodn.springboot.storage.db.member.repository.MemberJpaRepository;
import io.dodn.springboot.storage.db.plane.PlaneRepository;
import io.dodn.springboot.storage.db.plane.entity.Plane;
import io.dodn.springboot.storage.db.plane.repository.PlaneJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
        return planeJpaRepository.findAllByReceiverIdOrderByCreatedAtDesc(readerId);
    }

    @Override
    public boolean existsBySenderAndCreatedAtBetween(final Member sender, final LocalDateTime startOfDay, final LocalDateTime endOfDay) {
        return planeJpaRepository.existsBySenderAndCreatedAtBetween(sender, startOfDay, endOfDay);
    }

    @Override
    public List<Plane> findByReceiverIdAndCreatedAtBetween(final long readerId, final LocalDateTime startOfDay, final LocalDateTime endOfDay) {
        return planeJpaRepository.findByReceiverIdAndCreatedAtBetween(readerId, startOfDay, endOfDay);
    }

    @Override
    public long countBySenderIdAndCreatedAtBetween(final Long readerId, final LocalDateTime startOfDay, final LocalDateTime endOfDay) {
        return planeJpaRepository.countBySenderIdAndCreatedAtBetween(readerId, startOfDay, endOfDay);
    }
}
