package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    void updateStatus(@Param("orderId") String orderId, @Param("status") String status);

    @Modifying
    @Query("UPDATE Order o SET o.promotionApplied = :status WHERE o.id = :orderId")
    void updatePromotionAppliedStatus(@Param("orderId") String orderId, @Param("status") Boolean status);

    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findById(String id);
}
