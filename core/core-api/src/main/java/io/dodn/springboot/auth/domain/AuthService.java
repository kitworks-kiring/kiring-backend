package io.dodn.springboot.auth.domain;

import io.dodn.springboot.auth.jwt.JwtTokenProvider;
import io.dodn.springboot.auth.jwt.dto.TokenInfo;
import io.dodn.springboot.common.support.error.CoreException;
import io.dodn.springboot.common.support.error.ErrorType;
import io.dodn.springboot.storage.db.member.MemberRepository;
import io.dodn.springboot.storage.db.member.entity.Member;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }


    @Transactional // 사용자 정보 조회 시 트랜잭션 필요할 수 있음
    public TokenInfo refreshToken(final String refreshToken) {
        // 1. 리프레시 토큰 유효성 검증 및 클레임 추출
        // validateRefreshTokenAndGetClaims 메서드 내부에서 만료, 서명 오류 등 처리
        Claims claims = jwtTokenProvider.validationRefreshToeknAndGetClaims(refreshToken);

        // 2. 리프레시 토큰에서 사용자 ID 추출
        String memberIdStr = claims.getSubject();
        if (memberIdStr == null) {
            throw new CoreException(ErrorType.ERR_1099,"리프레시 토큰에 사용자 정보가 없습니다."); // ErrorType 수정
        }
        Long memberId = Long.valueOf(memberIdStr);

        // 4. 사용자 정보(권한 포함) 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CoreException(ErrorType.ERR_1099, "리프레시 토큰의 사용자를 찾을 수 없습니다.")); // ErrorType 수정

        // 5. 사용자 권한 가져오기 (Member 엔티티에서 권한 정보를 가져오는 로직 필요)
        Collection<? extends GrantedAuthority> authorities = getAuthoritiesForMember(member); // Helper 메서드
        // 6. 새로운 액세스 토큰 및 리프레시 토큰 생성
        return jwtTokenProvider.generateToken(memberIdStr, authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesForMember(Member member) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // Member 엔티티에 역할(Role) 정보가 있다고 가정
        // 예: member.getRole()이 "ADMIN" 또는 "USER" 반환
        if (member.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("USER"));

        return authorities;
    }
}
