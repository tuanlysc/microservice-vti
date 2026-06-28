package com.example.orderservice.client;

import com.example.orderservice.client.dto.reponse.ApplyPromotionResponse;
import com.example.orderservice.client.dto.request.ApplyPromotionRequest;

public interface PromotionClient {
    ApplyPromotionResponse apply(ApplyPromotionRequest request);

    void revoke(String orderId);
}
