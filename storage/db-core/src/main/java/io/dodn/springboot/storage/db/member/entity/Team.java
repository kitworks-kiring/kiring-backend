package io.dodn.springboot.storage.db.member.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TEAM")
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 ID 자동 생성
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name", nullable = false, length = 100)
    private String name;

    @Column(name = "", nullable = false, length = 100)
    private String code;

    protected Team() {
    }

    public Team(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return code;
    }


}
