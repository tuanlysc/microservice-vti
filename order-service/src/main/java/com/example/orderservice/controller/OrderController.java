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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    ResponseEntity<ApiResponse<List<OrderResponse>>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<OrderResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .message("get all orders")
                        .data(orderService.getAllOrders())
                        .build()
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<OrderResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message("get order by id")
                        .data(orderService.findById(id))
                        .build()
        );
    }
}
