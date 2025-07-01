package io.dodn.springboot.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Kiring API Documentation", version = "v1.0.1", description = "Kiring 프로젝트 API 명세서입니다.",
                contact = @Contact(name = "Kiring팀", email = "kiring.dev@gmail.com",
                        url = "https://kiring.example.com"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")),
        servers = {
                @Server(url = "https://dev-api.kiring.shop/", description = "Development Server"),
                @Server(url = "https://api.kiring.shop/", description = "OP Server"),
                @Server(url = "http://localhost:8080/", description = "Localhost Server")
        }
)
@Configuration
public class OpenApiConfig {
        @Bean
        public OpenAPI customOpenAPI() {
                // Security Scheme 정의
                SecurityScheme securityScheme = new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization");

                // Security Requirement 정의
                SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

                return new OpenAPI()
                        .info(new io.swagger.v3.oas.models.info.Info().title("Todolist API")
                                .description("Todolist Application API Documentation")
                                .version("v1.0"))
                        .addSecurityItem(securityRequirement)  // Security Requirement 추가
                        .schemaRequirement("BearerAuth", securityScheme);  // Security Scheme 추가
        }
}
