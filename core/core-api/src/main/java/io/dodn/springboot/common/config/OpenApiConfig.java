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
        servers = { @Server(url = "http://13.124.210.210/", description = "Development Server"),
                @Server(url = "localhost:8080/", description = "Localhost Server") })
@Configuration
public class OpenApiConfig {
}
