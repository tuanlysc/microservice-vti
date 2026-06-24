package com.example.orderservice.dto.response;

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
public class OrderItemResponse {
    String id;
    String productId;
    BigDecimal price;
    Integer quantity;
    Boolean isDeleted;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;
}
