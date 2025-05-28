package io.dodn.springboot.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정 적용
                .allowedOriginPatterns("http://localhost:*","https://localhost:*","http://43.202.199.39")
//                .allowedOrigins("http://localhost:3000", "http://localhost:8080", "http://43.202.199.39") // 허용할 출처 지정 여러개 가능
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.PATCH.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()) // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 요청 헤더 허용
                .allowCredentials(true) // 자격 증명(쿠키 등) 허용
                .maxAge(3600); // Preflight 요청 캐시 시간 (1시간)
    }
}
