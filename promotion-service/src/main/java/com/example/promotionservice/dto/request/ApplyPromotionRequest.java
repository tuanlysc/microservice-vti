package com.example.promotionservice.dto.request;

import com.example.promotionservice.common.DiscountType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyPromotionRequest {
    String code;
    BigDecimal orderAmount;
    String userId;
    String orderId;
}
