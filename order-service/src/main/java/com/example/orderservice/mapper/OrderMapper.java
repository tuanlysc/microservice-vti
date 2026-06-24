package com.example.orderservice.mapper;

import com.example.orderservice.dto.request.OrderCreateRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);

    Order toOrder(OrderCreateRequest request);
}
