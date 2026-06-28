package com.example.orderservice.client.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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
