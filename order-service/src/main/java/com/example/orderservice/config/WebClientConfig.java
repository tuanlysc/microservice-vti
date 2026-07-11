package com.example.orderservice.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("productWebClient")
    public WebClient productwebClient(WebClient.Builder webClientBuilder, @Value("${client.product.uri}") String baseUrl) {

        return webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Bean("promotionWebClient")
    public WebClient promotionwebClient(WebClient.Builder webClientBuilder, @Value("${client.promotion.uri}") String baseUrl) {

        return webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }
}
