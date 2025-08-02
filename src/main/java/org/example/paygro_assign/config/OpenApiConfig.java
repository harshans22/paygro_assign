package org.example.paygro_assign.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// com.cargopro.config.OpenApiConfig.java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Load & Booking API")
                .version("1.0.0")
                .description("API for load and booking management"));
    }
}

