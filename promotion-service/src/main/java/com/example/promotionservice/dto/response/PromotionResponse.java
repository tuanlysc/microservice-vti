package com.example.promotionservice.dto.response;

import com.example.promotionservice.common.DiscountType;
import com.example.promotionservice.common.PromotionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionResponse {
    String id;
    String code;
    String name;
    DiscountType discountType;
    BigDecimal discountValue;
    BigDecimal maxDiscountAmount;
    BigDecimal minOrderValue;
    Integer usageLimit;
    Integer usedCount;
    LocalDateTime startedAt;
    LocalDateTime endedAt;
    PromotionStatus status;
}
