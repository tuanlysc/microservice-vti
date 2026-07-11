package com.example.notificationservice.consumer.dto;

import java.time.Instant;

public class BaseEvent<T> {
    private String eventId;        // UUID, phục vụ idempotency
    private String eventType;      // "ORDER_CREATED", "PRODUCT_LOCK_RESULT"
    private String correlationId;  // xuyên suốt cả saga
    private String causationId;    // event nào gây ra event này
    private Instant occurredAt;    // thời điểm publish thực tế
    private Integer version;       // versioning schema
    private T payload;
}
