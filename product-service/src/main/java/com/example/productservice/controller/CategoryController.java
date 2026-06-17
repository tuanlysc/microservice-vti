package com.example.productservice.controller;

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    ResponseEntity<ApiResponse<CategoryResponse>> create(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CategoryResponse>builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Category Created")
                        .data(categoryService.createCategory(request))
                        .build()
        );
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<CategoryResponse>>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<CategoryResponse>>builder()
                        .code(HttpStatus.OK.value())
                        .data(categoryService.findAll())
                        .build()
        );
    }
}
