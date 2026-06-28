package com.example.promotionservice.repository;

import com.example.promotionservice.entity.PromotionUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, String> {
    Boolean existsByOrderId(String orderId);

    Optional<PromotionUsage> findByOrderId(String orderId);
}
