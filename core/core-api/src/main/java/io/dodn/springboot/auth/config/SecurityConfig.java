package io.dodn.springboot.auth.config;

import io.dodn.springboot.auth.jwt.JwtAuthenticationFilter;
import io.dodn.springboot.auth.jwt.JwtTokenProvider;
import io.dodn.springboot.auth.jwt.KakaoLogoutSuccessHandler;
import io.dodn.springboot.auth.jwt.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final KakaoLogoutSuccessHandler kakaoLogoutSuccessHandler; // 카카오 로그아웃 성공 핸들러

    public SecurityConfig(final JwtTokenProvider jwtTokenProvider, final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, final KakaoLogoutSuccessHandler kakaoLogoutSuccessHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.kakaoLogoutSuccessHandler = kakaoLogoutSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/login/oauth2/code/**",
                                "/oauth2/**", // OAuth2 인증 관련 API 접근 허용
                                "/login/oauth2/**", // 카카오 OAuth2 로그인 콜백
                                "/swagger-ui.html", // Swagger UI 접근 허용
                                "/swagger-ui/**", // Swagger UI 리소스 접근 허용
                                "/v3/api-docs/**", // Swagger API 문서 접근 허용
                                "/auth/logout", // 로그아웃 경로는 인증된 사용자가 호출할 수 있도록 authenticated()에 두거나,
                                "/api/v1/matzip/**",
                                "/api/v1/member/**"
                        ).permitAll()// API 접근 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler(kakaoLogoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "https://localhost:*", "http://43.202.199.39"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Preflight 요청 캐시 시간 (1시간)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
        return source;
    }


}
