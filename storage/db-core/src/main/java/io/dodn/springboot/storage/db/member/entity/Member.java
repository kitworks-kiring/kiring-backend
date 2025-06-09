package io.dodn.springboot.storage.db.member.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Entity
@Table(name = "MEMBER")
public class Member extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "kakao_id")
    private String kakaoId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "kiring_image_url")
    private String kiringImageUrl;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "github_id")
    private String githubId;

    @Column(name = "is_employed")
    private boolean isEmployed;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "joined_at")
    private LocalDate joinedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false) // MEMBER 테이블의 team_id 컬럼과 매핑, NULL
    private Team team;

    protected Member() {
    }

    public Member(String name, String email, String phone, String kakaoId, String nickname, String profileImageUrl, final String kiringImageUrl,
                  String birthday, String githubId, boolean isEmployed, boolean isAdmin, final LocalDate joinedAt, Team team) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.kiringImageUrl = kiringImageUrl;
        this.birthday = birthday;
        this.githubId = githubId;
        this.isEmployed = isEmployed;
        this.isAdmin = isAdmin;
        this.joinedAt = joinedAt;
        this.team = team;
    }

    public String getKiringImageUrl() {
        return kiringImageUrl;
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

    public String getKakaoId() {
        return kakaoId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getBirthday() {
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

    public Team getTeam() {
        return team;
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("이름은 비워둘 수 없습니다.");
        }
        this.name = newName;
    }

    public void changeEmail(String newEmail) {
        // 이메일 형식 검증 등의 로직 추가 가능
        if (newEmail == null || newEmail.isBlank()) { // 간단한 예시
            throw new IllegalArgumentException("이메일은 비워둘 수 없습니다.");
        }
        if (!isValidEmailFormat(newEmail)) { // 실제 이메일 형식 검증 로직
            throw new IllegalArgumentException("올바르지 않은 이메일 형식입니다.");
        }
        this.email = newEmail;
    }

    /**
     * 주어진 문자열이 유효한 이메일 형식인지 검사합니다.
     * @param email 검사할 이메일 문자열
     * @return 유효한 형식이면 true, 아니면 false
     */
    public static boolean isValidEmailFormat(String email) {
        if (email == null || email.isBlank()) {
            return false; // null 또는 빈 문자열은 유효하지 않음
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public void updateProfile(final Member member) {
        if (member.name != null && !member.name.isBlank()) {
            this.name = member.name;
        }
        if (member.email != null && !member.email.isBlank()) {
            this.email = member.email;
        }
        if (member.phone != null && !member.phone.isBlank()) {
            this.phone = member.phone;
        }
        if (member.kakaoId != null && !member.kakaoId.isBlank()) {
            this.kakaoId = member.kakaoId;
        }
        if (member.nickname != null && !member.nickname.isBlank()) {
            this.nickname = member.nickname;
        }
        if (member.profileImageUrl != null && !member.profileImageUrl.isBlank()) {
            this.profileImageUrl = member.profileImageUrl;
        }
        if (member.kiringImageUrl != null && !member.kiringImageUrl.isBlank()) {
            this.kiringImageUrl = member.kiringImageUrl;
        }
        if (member.birthday != null) {
            this.birthday = member.birthday;
        }
        if (member.githubId != null && !member.githubId.isBlank()) {
            this.githubId = member.githubId;
        }
        if (member.joinedAt != null) {
            this.joinedAt = member.joinedAt;
        }
        if (member.team != null) {
            this.team = member.team;
        }
        this.isEmployed = member.isEmployed;
    }
}
