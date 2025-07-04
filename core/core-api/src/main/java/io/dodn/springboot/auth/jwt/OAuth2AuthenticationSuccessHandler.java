package io.dodn.springboot.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dodn.springboot.auth.jwt.dto.TokenInfo;
import io.dodn.springboot.auth.kakao.dto.KakaoUserInfoResponse;
import io.dodn.springboot.common.support.error.ErrorType;
import io.dodn.springboot.member.domain.MemberService;
import io.dodn.springboot.member.exception.NotFoundMemberException;
import io.dodn.springboot.member.exception.NotFoundPhoneException;
import io.dodn.springboot.storage.db.member.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
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

//    @Value("${login.oauth2.redirect-uri}")
//    private String frontendTargetUrl;

    //TODO 프론트엔드 URL을 환경 변수로 관리하는 것이 좋습니다... 왜 안되지
//    private final String frontendTargetUrl = "https://kiring.vercel.app/login/callback"; // 또는 다른 프론트엔드 경로
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
            List<SimpleGrantedAuthority> authorities = getAuthoritiesForUser(member);

            final TokenInfo tokenInfo = jwtTokenProvider.generateToken(String.valueOf(member.getId()), authorities);
            log.info("frontendTargetUrl : {}", frontendTargetUrl);

            final String targetUrl = UriComponentsBuilder.fromUriString(frontendTargetUrl)
                            .queryParam("accessToken", tokenInfo.accessToken())
                            .queryParam("refreshToken", tokenInfo.refreshToken())
                            .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);

        } catch (NotFoundMemberException e) {
            // 3. 실패: 사용자를 찾을 수 없다는 예외 발생 시 회원가입 페이지로 리다이렉트 , 폰번호가 없는 경우도 추가
            log.info("가입되지 않은 사용자입니다. 추가 정보 입력 페이지로 리다이렉트합니다. Kakao User Info: {}", kakaoUserInfo);

            final String targetUrl = UriComponentsBuilder.fromUriString(frontendTargetUrl)
                    .queryParam("errorCode", ErrorType.ERR_1001)
                    .queryParam("errorMessage", ErrorType.ERR_1001.getMessage())
                    .build()
                    .encode() // URL 인코딩
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (NotFoundPhoneException e) {
            // 3. 실패: 사용자를 찾을 수 없다는 예외 발생 시 회원가입 페이지로 리다이렉트 , 폰번호가 없는 경우도 추가
            log.info("계정에 폰번호가 없습니다. 추가 정보 입력 페이지로 리다이렉트합니다. Kakao User Info: {}", kakaoUserInfo);

            final String targetUrl = UriComponentsBuilder.fromUriString(frontendTargetUrl)
                    .queryParam("errorCode", ErrorType.ERR_1003)
                    .queryParam("errorMessage", ErrorType.ERR_1003.getMessage())
                    .build()
                    .encode() // URL 인코딩
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (Exception e) {
            // 3. 실패: 계정에 폰번호가 없거나 나머지 서버 에러
            log.info("서버 에러 추가 정보 입력 페이지로 리다이렉트합니다. Kakao User Info: {}", kakaoUserInfo);

            final String targetUrl = UriComponentsBuilder.fromUriString(frontendTargetUrl)
                    .queryParam("errorCode", ErrorType.ERR_1099)
                    .queryParam("errorMessage", ErrorType.ERR_1099.getMessage())
                    .build()
                    .encode() // URL 인코딩
                    .toUriString();

            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } finally {
            clearAuthenticationAttributes(request);
        }
    }


    private List<SimpleGrantedAuthority> getAuthoritiesForUser(final Member member) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 사용자의 권한을 Enum으로 체크하여 권한을 리스트에 추가
        if (member.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return authorities;
    }
}
