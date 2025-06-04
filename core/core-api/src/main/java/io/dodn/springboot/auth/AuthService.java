package io.dodn.springboot.auth;

import io.dodn.springboot.auth.jwt.JwtTokenProvider;
import io.dodn.springboot.auth.jwt.dto.TokenInfo;
import io.dodn.springboot.auth.kakao.dto.KakaoTokenResponse;
import io.dodn.springboot.auth.kakao.dto.KakaoUserInfoResponse;
import io.dodn.springboot.auth.kakao.service.KakaoOAuthService;
import io.dodn.springboot.member.domain.MemberService;
import io.dodn.springboot.storage.db.member.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final KakaoOAuthService kakaoOAuthService;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(KakaoOAuthService kakaoOAuthService, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.kakaoOAuthService = kakaoOAuthService;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public TokenInfo loginWithKakao(String code) {
        // 카카오 OAuth 인증 후 받은 코드를 사용하여 토큰을 요청
        KakaoTokenResponse kakaoTokenResponse = kakaoOAuthService.getKakaoToken(code);
        log.info("카카오 token 정보 : {}", kakaoTokenResponse);

        // 카카오 사용자 정보를 가져옴
        KakaoUserInfoResponse kakaoUserInfo = kakaoOAuthService.getKakaoUserInfo(kakaoTokenResponse.accessToken());
        log.info("카카오 사용자 정보 : {}", kakaoUserInfo);

        // 사용자 정보를 기반으로 멤버를 생성하거나 조회
        Member member = memberService.findOrCreateMemberByKakaoInfo(kakaoUserInfo);
        log.info("멤버 정보 : {}", member);

        // Spring Security Authentication 객체 생성
        List<GrantedAuthority> authorities = getAuthoritiesForUser(member);

        // JWT 토큰 생성
        return jwtTokenProvider.generateToken(String.valueOf(member.getId()), authorities);
    }

    private List<GrantedAuthority> getAuthoritiesForUser(final Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 사용자의 권한을 Enum으로 체크하여 권한을 리스트에 추가
        if (member.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }

        return authorities;
    }
}
