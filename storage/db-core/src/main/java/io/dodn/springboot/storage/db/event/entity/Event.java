package io.dodn.springboot.storage.db.event.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import io.dodn.springboot.storage.db.common.entity.EventCategory;
import io.dodn.springboot.storage.db.member.entity.Member;
import io.dodn.springboot.storage.db.member.entity.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "event")
public class Event extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDatetime;

    @Column(nullable = false)
    private LocalDateTime endDatetime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventCategory eventCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    public Event(
            final String title,
            final String description,
            final LocalDateTime startDatetime,
            final LocalDateTime endDatetime,
            final EventCategory eventCategory,
            final Member creator,
            final Team team
    ) {
        this.title = title;
        this.description = description;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.eventCategory = eventCategory;
        this.creator = creator;
        this.team = team;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public Member getCreator() {
        return creator;
    }

    public Team getTeam() {
        return team;
    }
}
