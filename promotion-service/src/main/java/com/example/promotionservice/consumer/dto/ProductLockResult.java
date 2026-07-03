package com.example.promotionservice.consumer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductLockResult {
    String orderId;
    @NotEmpty
    Boolean isSuccess;
    String failReason;
}
