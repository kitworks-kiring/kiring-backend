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

    /**
     * JPA를 위한 protected 기본 생성자입니다.
     */
    protected Team() {
    }

    /**
     * Team 엔티티의 이름과 코드를 지정하여 인스턴스를 생성합니다.
     *
     * @param name 팀의 이름
     * @param code 팀의 코드
     */
    public Team(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * 팀의 고유 식별자를 반환합니다.
     *
     * @return 팀의 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 팀의 이름을 반환합니다.
     *
     * @return 팀 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 팀의 코드를 반환합니다.
     *
     * @return 팀 코드
     */
    public String getDescription() {
        return code;
    }

}
