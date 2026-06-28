package com.example.promotionservice.dto.response;

import com.example.promotionservice.common.DiscountType;
import com.example.promotionservice.common.PromotionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyPromotionResponse {
    String id;
    String code;
    DiscountType discountType;
    BigDecimal orderAmount;
    BigDecimal discountApplied;
    BigDecimal finalAmount;
    String userId;
    String orderId;
}
