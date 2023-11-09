package com.segurosbolivar.sanyuschedulingapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    /**
     * Creates and configures a RestTemplate instance.
     *
     * @return A RestTemplate object that can be used for making HTTP requests.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}