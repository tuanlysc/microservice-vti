package com.example.promotionservice.entity;

import com.example.promotionservice.common.DiscountType;
import com.example.promotionservice.common.PromotionStatus;
import jakarta.persistence.*;
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
@Entity
@Table(name = "promotions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Promotion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(unique = true)
    String code;

    String name;

    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    @Column(precision = 10, scale = 2)
    BigDecimal discountValue;

    @Column(precision = 10, scale = 2)
    BigDecimal maxDiscountAmount;

    @Column(precision = 10, scale = 2)
    @Builder.Default
    BigDecimal minOrderValue = BigDecimal.ZERO;

    Integer usageLimit;

    @Builder.Default
    Integer usedCount = 0;

    LocalDateTime startedAt;
    LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    PromotionStatus status = PromotionStatus.ACTIVE;
}
