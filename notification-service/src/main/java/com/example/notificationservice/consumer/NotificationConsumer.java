package com.example.notificationservice.consumer;

import com.example.notificationservice.common.NotificationChannel;
import com.example.notificationservice.common.NotificationStatus;
import com.example.notificationservice.common.NotificationType;
import com.example.notificationservice.consumer.dto.OrderCreateEvent;
import com.example.notificationservice.consumer.dto.ProductLockResult;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.repository.NotificationRepository;
import com.example.notificationservice.service.NotificationService;
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
public class NotificationConsumer {

    ObjectMapper objectMapper = new ObjectMapper();
    NotificationService emailNotificationSender;
    NotificationRepository notificationRepository;

    @KafkaListener(topics = "order_created")
    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Transactional
    public void handleOrderCreated(String message) throws JsonProcessingException {
        OrderCreateEvent event = objectMapper.readValue(message, OrderCreateEvent.class);

        // Save notification
        Notification notification = Notification.builder()
                .userId(event.getCustomerId())
                .type(NotificationType.ORDER_CREATED)
                .channel(NotificationChannel.EMAIL)
                .title("Đơn hàng đang được xử lý")
                .content("Đơn hàng <b>#" + event.getId() + "</b> đang chờ xác nhận.")
                .status(NotificationStatus.PENDING)
                .referenceId(event.getId())
                .referenceType("ORDER")
                .build();
        notificationRepository.save(notification);

        // Gửi email
        try {
            emailNotificationSender.send(
                    "tuan03dev@gmail.com",
                    notification.getTitle(),
                    notification.getContent()
            );
            notification.setStatus(NotificationStatus.SENT);
        } catch (Exception e) {
            notification.setRetryCount(notification.getRetryCount() + 1);
            notification.setStatus(NotificationStatus.FAILED);
        }
        notificationRepository.save(notification);
    }

    @KafkaListener(topics = "product.lock.result")
    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @Transactional
    public void handleProductLockResult(String message) throws JsonProcessingException {
        ProductLockResult result = objectMapper.readValue(message, ProductLockResult.class);

        String title = "Đơn hàng đã được xác nhận";
        String content = "Đơn hàng <b>#" + result.getOrderId() + "</b> đã được xác nhận.";
        if (!result.getIsSuccess()){
            title = "Đơn hàng bị hủy";
            content = "Đơn hàng <b>#" + result.getOrderId() + "</b> đã bị hủy với lý do: " + result.getFailReason();
        }
        emailNotificationSender.send(
                "tuan03dev@gmail.com",
                title,
                content
        );


    }
}
