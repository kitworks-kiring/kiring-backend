package io.dodn.springboot.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Kiring API Documentation", version = "v1.0.1", description = "Kiring 프로젝트 API 명세서입니다.",
                contact = @Contact(name = "Kiring팀", email = "kiring.dev@gmail.com",
                        url = "https://kiring.example.com"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")),
        servers = { @Server(url = "http://43.202.199.39", description = "Development Server"),
                @Server(url = "https://example.com", description = "Production Server") })
@Configuration
public class OpenApiConfig {

    // 필요하다면 여기에 @Bean으로 GroupedOpenApi 등을 추가로 정의하여 API 그룹핑 등을 할 수 있습니다.
    /*
     * @Bean public GroupedOpenApi publicApi() { return GroupedOpenApi.builder()
     * .group("public-apis") .pathsToMatch("/api/public/**") .build(); }
     *
     * @Bean public GroupedOpenApi adminApi() { return GroupedOpenApi.builder()
     * .group("admin-apis") .pathsToMatch("/api/admin/**") // .addOpenApiCustomizer(...)
     * // 더 세부적인 커스터마이징 .build(); }
     */

}
