package com.example.orderservice.controller;

import com.example.orderservice.dto.request.OrderCreateRequest;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping
    ResponseEntity<ApiResponse<OrderResponse>> create(@RequestBody @Valid OrderCreateRequest request){
        log.info("test controller");
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OrderResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Order created")
                        .data(orderService.create(request))
                        .build()
        );
    }
}
