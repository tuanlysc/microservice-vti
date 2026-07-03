package com.example.orderservice.service.impl;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.PromotionClient;
import com.example.orderservice.client.dto.reponse.ApplyPromotionResponse;
import com.example.orderservice.client.dto.reponse.ProductResponse;
import com.example.orderservice.client.dto.request.ApplyPromotionRequest;
import com.example.orderservice.client.dto.request.LockProductItemRequest;
import com.example.orderservice.client.dto.request.LockProductRequest;
import com.example.orderservice.client.dto.request.ProductFilter;
import com.example.orderservice.dto.request.OrderCreateRequest;
import com.example.orderservice.dto.request.OrderItemCreateRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.event.OrderCreateEvent;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ErrorCode;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.orderservice.common.OrderStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderMapper orderMapper;
    OrderRepository orderRepository;
    ProductClient productClient;
    PromotionClient promotionClient;
    KafkaTemplate<String, Object> orderKafkaTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse create(OrderCreateRequest request) {
        // Fetch products
        List<String> productIds = request.getOrderItemCreateRequests().stream()
                .map(OrderItemCreateRequest::getProductId)
                .distinct()
                .toList();

        List<ProductResponse> products = productClient.search(
                ProductFilter.builder().productIds(productIds).build());

        Map<String, ProductResponse> productsMap = products.stream()
                .collect(Collectors.toMap(ProductResponse::getId, p -> p));

        // Build OrderItems
        List<OrderItem> orderItems = request.getOrderItemCreateRequests().stream()
                .map(req -> {
                    ProductResponse product = productsMap.get(req.getProductId());
                    if (product == null) {
                        throw new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND);
                    }
                    if (product.getStock() < req.getQuantity()) {
                        throw new ApplicationException(ErrorCode.NOT_ENOUGH_STOCK);
                    }
                    return OrderItem.builder()
                            .productId(product.getId())
                            .price(product.getPrice())
                            .quantity(req.getQuantity())
                            .build();
                })
                .toList();

        // Tính tổng tiền
        BigDecimal totalPrice = orderItems.stream()
                .map(oi -> oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Save order lần đầu để lấy id
        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .status(PENDING.toString())
                .totalAmount(totalPrice)
                .build();
        orderItems.forEach(oi -> oi.setOrder(order));
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // Áp dụng promotion
        if (request.getPromotionCode() != null && !request.getPromotionCode().isBlank()) {
            try {
                ApplyPromotionResponse promotionResponse = promotionClient.apply(
                        ApplyPromotionRequest.builder()
                                .code(request.getPromotionCode())
                                .orderAmount(totalPrice)
                                .userId(request.getCustomerId())
                                .orderId(savedOrder.getId())
                                .build());

                savedOrder.setTotalAmount(promotionResponse.getFinalAmount());
                savedOrder.setPromotionApplied(true);
                orderRepository.updatePromotionAppliedStatus(savedOrder.getId(),true);
            } catch (Exception e) {
                log.error("promotionClient.apply() fail - code={}, error={}",
                        request.getPromotionCode(), e.getMessage(), e);
                throw new ApplicationException(ErrorCode.APPLY_PROMOTION_ERROR);
            }
        }

        // Lock stock

        OrderCreateEvent orderCreateEvent = orderMapper.toEvent(savedOrder);
        orderKafkaTemplate.send("order_created", orderCreateEvent);
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse findById(String id) {
        return orderRepository.findById(id).map(orderMapper::toResponse).orElseThrow(
                () -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND)
        );
    }


}