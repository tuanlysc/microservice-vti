package com.example.orderservice.mapper;

import com.example.orderservice.dto.response.OrderItemResponse;
import com.example.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemResponse toResponse(OrderItem orderItem);
}
