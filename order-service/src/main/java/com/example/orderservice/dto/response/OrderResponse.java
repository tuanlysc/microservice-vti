package com.example.orderservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    String customerId;
    String status;
    Boolean promotionApplied;
    BigDecimal totalAmount;
    Boolean isDeleted;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;
    List<OrderItemResponse> orderItemResponses;
}
