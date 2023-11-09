package com.segurosbolivar.sanyuschedulingapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Value("${frontend.url}")
    private String frontendUrl;

    /**
     * Configures CORS to allow requests from the specified frontend URL and allows all HTTP methods for all endpoints under "/api".
     *
     * @return A WebMvcConfigurer instance with the CORS configuration.
     */
    @Bean
    public WebMvcConfigurer corsConfigurator() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(frontendUrl)
                        .allowedMethods("*");
            }
        };
    }

}