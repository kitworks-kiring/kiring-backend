package io.dodn.springboot.storage.db.matzip.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import io.dodn.springboot.storage.db.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "place_like",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_place_like_member_place",
                        columnNames = {"member_id", "place_id"}
                )
        })
public class PlaceLike extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    public PlaceLike(Member member, Place place) {
        this.member = member;
        this.place = place;
    }

}
