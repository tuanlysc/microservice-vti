package com.example.orderservice.consumer;

import com.example.orderservice.client.PromotionClient;
import com.example.orderservice.common.OrderStatus;
import com.example.orderservice.consumer.dto.ProductLockResult;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ErrorCode;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LockProductResultConsumer {
    ObjectMapper objectMapper;
    OrderRepository orderRepository;
    PromotionClient  promotionClient;

    @KafkaListener(topics = "product.lock.result")
    public void handleLockResult(String objectString) throws JsonProcessingException {
        ProductLockResult result = objectMapper.readValue(objectString, ProductLockResult.class);

        Order order = orderRepository.findById(result.getOrderId()).orElseThrow(
                () -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND)
        );

        if(result.getIsSuccess()) {
            orderRepository.updateStatus(order.getId(), OrderStatus.CONFIRMED.toString());
        }
        else  {
            orderRepository.updateStatus(order.getId(), OrderStatus.FAILED.toString());
            if(order.getPromotionApplied()){
                promotionClient.revoke(order.getId());
            }
        }

    }
}
