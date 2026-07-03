package com.example.orderservice.consumer;

import com.example.orderservice.client.PromotionClient;
import com.example.orderservice.common.OrderStatus;
import com.example.orderservice.consumer.dto.ProductLockResult;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ErrorCode;
import com.example.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LockProductResultConsumer {
    ObjectMapper objectMapper;
    OrderRepository orderRepository;

    @KafkaListener(topics = "product.lock.result")
    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Transactional()
    public void handleLockResult(String objectString) throws JsonProcessingException {
        ProductLockResult result = objectMapper.readValue(objectString, ProductLockResult.class);

        Order order = orderRepository.findById(result.getOrderId()).orElseThrow(
                () -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND)
        );

        if(Boolean.TRUE.equals(result.getIsSuccess())) {
            orderRepository.updateStatus(order.getId(), OrderStatus.CONFIRMED.toString());
        }
        else  {
            orderRepository.updateStatus(order.getId(), OrderStatus.FAILED.toString());
        }

    }
}
