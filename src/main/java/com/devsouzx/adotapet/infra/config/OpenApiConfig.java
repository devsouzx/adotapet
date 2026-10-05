package com.devsouzx.adotapet.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI adotapetOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AdotaPet API")
                        .version("v1")
                        .description("API para cadastro de abrigos, pets, adotantes e gerenciamento de adoções."))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Informe o token JWT recebido no login.")));
    }
}
