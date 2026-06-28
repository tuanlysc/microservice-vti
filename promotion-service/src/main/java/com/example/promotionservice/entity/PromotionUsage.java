package com.example.promotionservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "promotion_usages")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    Promotion promotion;

    String userId;
    String orderId;
    @Column(precision = 10, scale = 2)
    BigDecimal discountApplied;
    @Column(precision = 10, scale = 2)
    BigDecimal orderAmount;
    @CreatedDate
    Instant usedAt;
}
