package io.dodn.springboot.auth.jwt;

import io.dodn.springboot.auth.jwt.dto.TokenInfo;
import io.dodn.springboot.common.support.error.CoreException;
import io.dodn.springboot.common.support.error.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = ACCESS_TOKEN_EXPIRE_TIME * 2; // Refresh token은 2배로 설정
    private Key key;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    public void init(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(String memberId, Collection<? extends GrantedAuthority>  authentication) {
        String authorities = authentication.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        String accessToken = Jwts.builder()
                .setSubject(memberId)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(memberId)
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME)) // Refresh token은 2배로 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenInfo(
                BEARER_TYPE,
                accessToken,
                refreshToken,
                ACCESS_TOKEN_EXPIRE_TIME
        );
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new CoreException(ErrorType.FAILED_KAKAO, "권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명: {}", e.getMessage());
            throw new CoreException(ErrorType.FAILED_KAKAO, "잘못된 JWT 서명");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWt 토큰: {}", e.getMessage());
            throw new CoreException(ErrorType.FAILED_KAKAO, "만료된 JWt 토큰");
        } catch (Exception e) {
            log.error("JWT token 오류: {}", e.getMessage());
            throw new CoreException(ErrorType.FAILED_KAKAO, "JWT token 오류");
        }
    }

    private Claims parseClaims(final String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
            throw new CoreException(ErrorType.FAILED_KAKAO, "Expired JWT token");
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new CoreException(ErrorType.FAILED_KAKAO, "Invalid JWT token");
        }
    }

}
