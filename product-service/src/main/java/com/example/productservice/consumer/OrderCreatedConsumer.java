package com.example.productservice.consumer;

import com.example.productservice.consumer.dto.OrderCreateEvent;
import com.example.productservice.dto.request.LockProductItemRequest;
import com.example.productservice.dto.request.LockProductRequest;
import com.example.productservice.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderCreatedConsumer {
    ProductService productService;
    ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "order_created")
    public void handleOrderCreateEvent(String orderString) throws JsonProcessingException {
        OrderCreateEvent orderCreateEvent = objectMapper.readValue(orderString, OrderCreateEvent.class);

        List<LockProductItemRequest> lockProductItemRequests = orderCreateEvent.getItems().stream()
                .map(orderItemEvent -> LockProductItemRequest.builder()
                        .productId(orderItemEvent.getProductId())
                        .quantity(orderItemEvent.getQuantity())
                        .build()).toList();

        LockProductRequest lockProductRequest = LockProductRequest.builder()
                .orderId(orderCreateEvent.getId())
                .items(lockProductItemRequests)
                .build();

        productService.lock(lockProductRequest);
    }
}
