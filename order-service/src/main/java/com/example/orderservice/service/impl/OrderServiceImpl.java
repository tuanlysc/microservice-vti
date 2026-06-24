package com.example.orderservice.service.impl;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.dto.reponse.ProductResponse;
import com.example.orderservice.client.dto.request.ProductFilter;
import com.example.orderservice.dto.request.OrderCreateRequest;
import com.example.orderservice.dto.request.OrderItemCreateRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ErrorCode;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderMapper orderMapper;
    OrderRepository orderRepository;
    ProductClient productClient;
    @Override
    public OrderResponse create(OrderCreateRequest request) {
        List<String> productIds = request.getOrderItemCreateRequests()
                .stream()
                .map(OrderItemCreateRequest::getProductId)
                .distinct()
                .toList();

        ProductFilter productFilter = ProductFilter.builder()
                .productIds(productIds)
                .build();

        List<ProductResponse> products = productClient.search(productFilter);

        Map<String, ProductResponse> productsMap = products.stream()
                .collect(Collectors.toMap(ProductResponse::getId, p -> p));

        List<OrderItem> orderItems = request.getOrderItemCreateRequests()
                .stream()
                .map(orderItemCreateRequest ->  {
                    ProductResponse productResponse = productsMap.get(orderItemCreateRequest.getProductId());
                    if(productResponse == null){
                        throw new ApplicationException(ErrorCode.DUPLICATE_KEY_PRODUCT);
                    }

                    return OrderItem.builder()
                            .productId(productResponse.getId())
                            .price(productResponse.getPrice())
                            .quantity(orderItemCreateRequest.getQuantity())
                            .build();
                })
                .toList();

        BigDecimal totalPrice = orderItems.stream()
                .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .status("PENDING")
                .totalAmount(totalPrice)
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setItems(orderItems);

        return orderMapper.toResponse(orderRepository.save(order));
    }
}
