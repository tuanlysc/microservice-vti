package com.example.promotionservice.controller;

import com.example.promotionservice.dto.request.ApplyPromotionRequest;
import com.example.promotionservice.dto.request.CreatePromotionRequest;
import com.example.promotionservice.dto.response.ApiResponse;
import com.example.promotionservice.dto.response.ApplyPromotionResponse;
import com.example.promotionservice.dto.response.PromotionResponse;
import com.example.promotionservice.service.PromotionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionController {
    PromotionService promotionService;

    @PostMapping
    ResponseEntity<ApiResponse<PromotionResponse>> create(@RequestBody @Valid CreatePromotionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<PromotionResponse>builder()
                        .data(promotionService.create(request))
                        .build()
        );
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<PromotionResponse>>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<PromotionResponse>>builder()
                        .data(promotionService.findAll())
                        .build()
        );
    }

    @GetMapping("/{code}")
    ResponseEntity<ApiResponse<PromotionResponse>> findByCode(@PathVariable String code){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<PromotionResponse>builder()
                        .data(promotionService.findByCode(code))
                        .build()
        );
    }

    @PostMapping("/apply")
    ResponseEntity<ApiResponse<ApplyPromotionResponse>> apply(@RequestBody @Valid ApplyPromotionRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<ApplyPromotionResponse>builder()
                        .data(promotionService.apply(request))
                        .build()
        );
    }

    @PostMapping("/revoke/{orderId}")
    ResponseEntity<ApiResponse<Void>> revoke(@PathVariable String orderId){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<Void>builder()
                        .data(promotionService.revoke(orderId))
                        .message("revoked")
                        .build()
        );
    }
}
