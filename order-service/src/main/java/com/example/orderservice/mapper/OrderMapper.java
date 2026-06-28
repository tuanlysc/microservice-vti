package com.example.orderservice.mapper;

import com.example.orderservice.dto.request.OrderCreateRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.event.OrderCreateEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);

    Order toOrder(OrderCreateRequest request);

    OrderCreateEvent toEvent(Order order);
}
