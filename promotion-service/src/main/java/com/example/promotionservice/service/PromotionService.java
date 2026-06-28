package com.example.promotionservice.service;

import com.example.promotionservice.dto.request.ApplyPromotionRequest;
import com.example.promotionservice.dto.request.CreatePromotionRequest;
import com.example.promotionservice.dto.response.ApplyPromotionResponse;
import com.example.promotionservice.dto.response.PromotionResponse;

import java.util.List;

public interface PromotionService {
    PromotionResponse create(CreatePromotionRequest request);

    List<PromotionResponse> findAll();

    PromotionResponse findByCode(String code);

    ApplyPromotionResponse apply(ApplyPromotionRequest request);

    Void revoke(String orderId);



}
