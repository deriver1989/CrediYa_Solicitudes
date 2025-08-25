package co.com.pragma.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi solicitudApi() {
        return GroupedOpenApi.builder()
                .group("solicitud-api")
                .pathsToMatch("/api/v1/solicitud/**")
                .build();
    }
}