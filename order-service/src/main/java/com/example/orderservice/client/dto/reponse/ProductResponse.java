package com.example.orderservice.client.dto.reponse;

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
public class ProductResponse {
    String id;
    String name;
    BigDecimal price;
    Integer stock;
    String categoryId;
    Boolean isDeleted;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;
}
