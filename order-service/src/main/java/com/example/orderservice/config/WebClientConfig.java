package com.example.orderservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean("productWebClient")
    public WebClient productwebClient(@Value("${services.product-service}") String baseUrl) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean("promotionWebClient")
    public WebClient promotionwebClient(@Value("${services.promotion-service}") String baseUrl) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
