package com.example.orderservice.client.impl;

import com.example.orderservice.client.PromotionClient;
import com.example.orderservice.client.dto.reponse.ApplyPromotionResponse;
import com.example.orderservice.client.dto.request.ApplyPromotionRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionClientImpl implements PromotionClient {

    WebClient webClient;
    public PromotionClientImpl(@Qualifier("promotionWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ApplyPromotionResponse apply(ApplyPromotionRequest request) {
        ApiResponse<ApplyPromotionResponse> response =
                webClient.post()
                        .uri("/promotion/apply")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<ApiResponse<ApplyPromotionResponse>>() {})
                        .block();

        if (response == null || response.getData() == null) {
            throw new ApplicationException(ErrorCode.APPLY_PROMOTION_ERROR);
        }
        return response.getData();
    }

    @Override
    public void revoke(String orderId) {
        webClient.post()
                .uri("/promotion/revoke/{orderId}", orderId)
                .retrieve()
                .toBodilessEntity()
                .block();

    }
}
