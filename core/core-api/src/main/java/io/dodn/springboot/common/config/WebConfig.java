package io.dodn.springboot.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 애플리케이션의 모든 경로에 대해 CORS(Cross-Origin Resource Sharing) 정책을 설정합니다.
     *
     * 로컬호스트(HTTP/HTTPS, 모든 포트)와 특정 IP(`http://43.202.199.39`)에서의 요청을 허용하며,
     * GET, PATCH, POST, PUT, DELETE 메서드를 사용할 수 있습니다. 모든 요청 헤더가 허용되고,
     * 자격 증명(쿠키 등)도 포함할 수 있습니다. Preflight 요청의 캐시 유효 시간은 3600초(1시간)입니다.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정 적용
            .allowedOriginPatterns("http://localhost:*", "https://localhost:*", "http://43.202.199.39")
            .allowedMethods(HttpMethod.GET.name(), HttpMethod.PATCH.name(), HttpMethod.POST.name(),
                    HttpMethod.PUT.name(), HttpMethod.DELETE.name()) // 허용할 HTTP 메서드
            .allowedHeaders("*") // 모든 요청 헤더 허용
            .allowCredentials(true) // 자격 증명(쿠키 등) 허용
            .maxAge(3600); // Preflight 요청 캐시 시간 (1시간)
    }

}
