package io.dodn.springboot.storage.db.member.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "MEMBER")
public class Member extends BaseEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "member_name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "kakako_id")
    private String kakakoId;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "profileImageUrl")
    private String profileImageUrl;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "githubId")
    private String githubId;
    @Column(name = "isEmployed")
    private boolean isEmployed;
    @Column(name = "isAdmin")
    private boolean isAdmin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false) // MEMBER 테이블의 team_id 컬럼과 매핑, NULL 비허용
    private Team team;


    protected Member() {
    }

    public Member(String name, String email, String phone, String kakakoId, String nickname, String profileImageUrl,
                  LocalDate birthday, String githubId, boolean isEmployed, boolean isAdmin, Team team) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.kakakoId = kakakoId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.birthday = birthday;
        this.githubId = githubId;
        this.isEmployed = isEmployed;
        this.isAdmin = isAdmin;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getKakakoId() {
        return kakakoId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public LocalDate getBirthdate() {
        return birthday;
    }

    public String getGithubId() {
        return githubId;
    }

    public boolean isEmployed() {
        return isEmployed;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
