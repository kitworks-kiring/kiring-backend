package io.dodn.springboot.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dodn.springboot.auth.jwt.dto.TokenInfo;
import io.dodn.springboot.auth.kakao.dto.KakaoUserInfoResponse;
import io.dodn.springboot.common.support.error.ErrorType;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.member.domain.MemberService;
import io.dodn.springboot.member.exception.NotFoundMemberException;
import io.dodn.springboot.storage.db.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final static Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper; // ★★★ ObjectMapper 주입 ★★★
    private final MemberService memberService;

    private final String frontendTargetUrl = "http://localhost:3000/login/callback"; // 또는 다른 프론트엔드 경로

    public OAuth2AuthenticationSuccessHandler(final JwtTokenProvider jwtTokenProvider, final ObjectMapper objectMapper, final MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.memberService = memberService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            log.info("OAuth2 로그인 성공. 사용자 정보 Attributes: {}", oAuth2User.getAttributes());
            log.info("OAuth2 로그인 성공. 사용자 이름 ( getName() ): {}", oAuth2User.getName()); // 보통 고유 ID

            final KakaoUserInfoResponse kakaoUserInfo = objectMapper.convertValue(attributes, KakaoUserInfoResponse.class);
            log.info("Kakao User Info (converted): ID={}, Nickname={}, Email={}", kakaoUserInfo.id(), kakaoUserInfo.getNickname(), kakaoUserInfo.getEmail());
        try {
            Member member = memberService.findOrCreateMemberByKakaoInfo(kakaoUserInfo);
            List<GrantedAuthority> authorities = getAuthoritiesForUser(member);

            final TokenInfo tokenInfo = jwtTokenProvider.generateToken(String.valueOf(member.getId()), authorities);
            log.info("애플리케이션 JWT 발급: {}", tokenInfo);

            final String targetUrl = UriComponentsBuilder.fromUriString(frontendTargetUrl)
                            .queryParam("accessToken", tokenInfo.accessToken())
                            .queryParam("refreshToken", tokenInfo.refreshToken())
                            .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (NotFoundMemberException e) {
            // 3. 실패: 사용자를 찾을 수 없다는 예외 발생 시 회원가입 페이지로 리다이렉트
            log.info("가입되지 않은 사용자입니다. 추가 정보 입력 페이지로 리다이렉트합니다. Kakao User Info: {}", kakaoUserInfo);

            final String targetUrl = UriComponentsBuilder.fromUriString(frontendTargetUrl)
                    .queryParam("email", kakaoUserInfo.getEmail())
                    .queryParam("nickname", kakaoUserInfo.getNickname())
                    .queryParam("profileImageUrl", kakaoUserInfo.getProfileImageUrl())
                    .queryParam("kakaoId", String.valueOf(kakaoUserInfo.id()))
                    .build()
                    .encode() // URL 인코딩
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = ApiResponse.error(ErrorType.DEFAULT_ERROR, e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        } finally {
            clearAuthenticationAttributes(request);
        }
    }


    private List<GrantedAuthority> getAuthoritiesForUser(final Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 사용자의 권한을 Enum으로 체크하여 권한을 리스트에 추가
        if (member.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }
}
