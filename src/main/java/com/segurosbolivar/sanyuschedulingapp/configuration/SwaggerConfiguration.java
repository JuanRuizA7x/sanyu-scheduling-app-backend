package com.segurosbolivar.sanyuschedulingapp.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    /**
     * Creates and configures an instance of OpenAPI to define the documentation for the SANYU Scheduling App. It includes information such as the application title, description, version, and licensing details. Additionally, it provides external documentation links for reference.
     *
     * @return An OpenAPI object representing the custom documentation for the application.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("SANYU Scheduling App")
                        .description("Application for the management of work shifts of the contractors of the SANYU company")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }

}