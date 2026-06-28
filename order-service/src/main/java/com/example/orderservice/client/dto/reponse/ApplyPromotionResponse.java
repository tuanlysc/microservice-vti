package com.example.orderservice.client.dto.reponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyPromotionResponse {
    String id;
    String code;
    String discountType;
    BigDecimal orderAmount;
    BigDecimal discountApplied;
    BigDecimal finalAmount;
    String userId;
    String orderId;
}
