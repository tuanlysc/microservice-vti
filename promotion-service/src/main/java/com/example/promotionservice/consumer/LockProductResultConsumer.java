package com.example.promotionservice.consumer;

import com.example.promotionservice.consumer.dto.ProductLockResult;
import com.example.promotionservice.repository.PromotionRepository;
import com.example.promotionservice.service.PromotionService;
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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LockProductResultConsumer {
    ObjectMapper objectMapper;
    PromotionService promotionService;
    PromotionRepository promotionRepository;

    @KafkaListener(topics = "product.lock.result")
    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public void handleLockResult(String objectString) throws JsonProcessingException {
        ProductLockResult result = objectMapper.readValue(objectString, ProductLockResult.class);

        if(Boolean.FALSE.equals(result.getIsSuccess()) && promotionRepository.existsById(result.getOrderId())){
            promotionService.revoke(result.getOrderId());
        }

    }
}
