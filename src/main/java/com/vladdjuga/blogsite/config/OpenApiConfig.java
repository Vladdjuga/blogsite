package com.vladdjuga.blogsite.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blogsite API")
                        .description("REST API for Blogsite application. Authentication via HttpOnly cookie 'accessToken'. Use /api/auth/login to get the cookie. Or use /api/auth/token to get the token directly.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Vladyslav Dzhuha")))
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"))
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", new SecurityScheme()
                                .name("accessToken")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .description("JWT token in HttpOnly cookie. Login via /api/auth/login to set the cookie.")));
    }
}

