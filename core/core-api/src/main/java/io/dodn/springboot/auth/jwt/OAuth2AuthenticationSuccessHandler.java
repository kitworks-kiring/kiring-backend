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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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

    private final String frontendTargetUrl = "http://localhost:3000/auth/oauth2/success"; // 또는 다른 프론트엔드 경로

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
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            log.info("OAuth2 로그인 성공. 사용자 정보 Attributes: {}", oAuth2User.getAttributes());
            log.info("OAuth2 로그인 성공. 사용자 이름 ( getName() ): {}", oAuth2User.getName()); // 보통 고유 ID

            final KakaoUserInfoResponse kakaoUserInfo = objectMapper.convertValue(attributes, KakaoUserInfoResponse.class);
            log.info("Kakao User Info (converted): ID={}, Nickname={}, Email={}", kakaoUserInfo.id(), kakaoUserInfo.getNickname(), kakaoUserInfo.getEmail());

            Member member = memberService.findOrCreateMemberByKakaoInfo(kakaoUserInfo);
            List<GrantedAuthority> authorities = getAuthoritiesForUser(member);

            final TokenInfo tokenInfo = jwtTokenProvider.generateToken(String.valueOf(member.getId()), authorities);
            log.info("애플리케이션 JWT 발급: {}", tokenInfo);

            // --- JSON 응답으로 직접 TokenInfo 보내기 ---
            response.setStatus(HttpStatus.OK.value()); // HTTP 상태 코드 200 OK
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); // Content Type을 JSON으로 설정
            response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정

            // TokenInfo 객체를 JSON 문자열로 변환하여 응답 본문에 작성
            objectMapper.writeValue(response.getWriter(), tokenInfo);
            clearAuthenticationAttributes(request);
        } catch (NotFoundMemberException e) {
            ApiResponse<?> errorResponse = ApiResponse.error(ErrorType.OAUTH_LOGIN_FAILED, e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = ApiResponse.error(ErrorType.DEFAULT_ERROR, e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }

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
