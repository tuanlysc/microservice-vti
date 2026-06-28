package com.example.promotionservice.mapper;

import com.example.promotionservice.dto.request.ApplyPromotionRequest;
import com.example.promotionservice.dto.request.CreatePromotionRequest;
import com.example.promotionservice.dto.response.PromotionResponse;
import com.example.promotionservice.entity.Promotion;
import com.example.promotionservice.entity.PromotionUsage;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    PromotionResponse toResponse(Promotion promotion);

    Promotion toEntity(CreatePromotionRequest request);

    default PromotionUsage toUsage(ApplyPromotionRequest request, Promotion promotion, BigDecimal discount){
        return PromotionUsage.builder()
                .promotion(promotion)
                .userId(request.getUserId())
                .discountApplied(discount)
                .orderAmount(request.getOrderAmount())
                .build();
    }
}
