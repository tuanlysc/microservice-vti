package com.example.orderservice.event;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateEvent {

    private String id;
    private String customerId;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemEvent> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemEvent {
        private String productId;
        private int quantity;
        private BigDecimal price;
    }
}