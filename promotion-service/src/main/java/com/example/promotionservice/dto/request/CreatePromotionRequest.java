package com.example.promotionservice.dto.request;

import com.example.promotionservice.common.DiscountType;
import com.example.promotionservice.config.EndDateDeserializer;
import com.example.promotionservice.config.StartDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePromotionRequest {
    @NotBlank(message = "Code is required")
    String code;

    String name;

    @NotNull(message = "Discount type is required")
    DiscountType discountType;

    @NotNull(message = "Discount value is required")
    BigDecimal discountValue;

    BigDecimal maxDiscountAmount;

    BigDecimal minOrderValue;

    @Min(value = 1, message = "Usage limit must be at least 1")
    Integer usageLimit;

    @JsonDeserialize(using = StartDateDeserializer.class)
    LocalDateTime startedAt;

    @JsonDeserialize(using = EndDateDeserializer.class)
    LocalDateTime endedAt;
}
