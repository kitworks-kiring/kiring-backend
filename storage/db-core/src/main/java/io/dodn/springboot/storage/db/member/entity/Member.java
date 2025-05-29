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
import java.util.regex.Pattern;

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
    @JoinColumn(name = "team_id", nullable = false) // MEMBER 테이블의 team_id 컬럼과 매핑, NULL
                                                    // 비허용
    private Team team;

    /**
     * JPA를 위한 기본 생성자입니다.
     */
    protected Member() {
    }

    /**
     * 주어진 회원 정보를 사용하여 새로운 Member 엔티티를 생성합니다.
     *
     * @param name 회원 이름
     * @param email 회원 이메일 주소
     * @param phone 회원 전화번호
     * @param kakakoId 회원의 카카오 ID
     * @param nickname 회원 닉네임
     * @param profileImageUrl 회원 프로필 이미지 URL
     * @param birthday 회원 생년월일
     * @param githubId 회원의 GitHub ID
     * @param isEmployed 회원의 재직 여부
     * @param isAdmin 회원의 관리자 여부
     * @param team 소속 팀 엔티티
     */
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

    /**
     * 회원의 고유 식별자 값을 반환합니다.
     *
     * @return 회원의 ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 회원의 이름을 반환합니다.
     *
     * @return 회원 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 회원의 이메일 주소를 반환합니다.
     *
     * @return 이메일 주소
     */
    public String getEmail() {
        return email;
    }

    /**
     * 회원의 전화번호를 반환합니다.
     *
     * @return 전화번호 문자열
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 회원의 카카오 아이디를 반환합니다.
     *
     * @return 카카오 아이디 문자열
     */
    public String getKakakoId() {
        return kakakoId;
    }

    /**
     * 회원의 닉네임을 반환합니다.
     *
     * @return 닉네임 문자열
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 회원의 프로필 이미지 URL을 반환합니다.
     *
     * @return 프로필 이미지의 URL
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * 회원의 생년월일을 반환합니다.
     *
     * @return 회원의 생년월일
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * 회원의 GitHub ID를 반환합니다.
     *
     * @return GitHub ID 문자열
     */
    public String getGithubId() {
        return githubId;
    }

    /**
     * 회원의 재직 여부를 반환합니다.
     *
     * @return 재직 중이면 true, 아니면 false
     */
    public boolean isEmployed() {
        return isEmployed;
    }

    /**
     * 회원이 관리자인지 여부를 반환합니다.
     *
     * @return 관리자인 경우 true, 아니면 false
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * 회원이 소속된 팀 엔티티를 반환합니다.
     *
     * @return 팀 엔티티 객체
     */
    public Team getTeam() {
        return team;
    }

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 회원의 이름을 변경합니다.
     *
     * @param newName 변경할 새로운 이름
     * @throws IllegalArgumentException newName이 null이거나 비어 있는 경우 발생합니다.
     */
    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("이름은 비워둘 수 없습니다.");
        }
        this.name = newName;
    }

    /****
     * 회원의 이메일 주소를 새 값으로 변경합니다.
     *
     * @param newEmail 변경할 이메일 주소
     * @throws IllegalArgumentException newEmail이 null, 비어 있거나 올바르지 않은 이메일 형식일 경우 발생합니다.
     */
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
     * 입력된 문자열이 유효한 이메일 형식인지 확인합니다.
     *
     * @param email 검사할 이메일 주소 문자열
     * @return 이메일 형식이 올바르면 true, 그렇지 않으면 false
     */
    public static boolean isValidEmailFormat(String email) {
        if (email == null || email.isBlank()) {
            return false; // null 또는 빈 문자열은 유효하지 않음
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
