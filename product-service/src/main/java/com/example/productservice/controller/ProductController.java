package com.example.productservice.controller;

import com.example.productservice.dto.request.LockProductRequest;
import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductFilter;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping
    ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody @Valid ProductCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ProductResponse>builder()
                        .code(201)
                        .message("Product Created")
                        .data(productService.create(request))
                        .build()
        );
    }

    @GetMapping
    ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProduct(){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<ProductResponse>>builder()
                        .code(200)
                        .data(productService.findAll())
                        .build()
        );
    }

    @GetMapping("/category/{id}")
    ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProduct(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<ProductResponse>>builder()
                        .code(200)
                        .data(productService.findAllByCategoryId(id))
                        .build()
        );
    }

    @GetMapping("/name")
    ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProductByName(@RequestParam String name){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<ProductResponse>>builder()
                        .code(200)
                        .data(productService.findByName(name))
                        .build()
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable String id, @RequestBody @Valid ProductUpdateRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<ProductResponse>builder()
                        .code(200)
                        .data(productService.update(id, request))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<ProductResponse>> deleteProduct(@PathVariable String id){
        productService.deleteProductById(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<ProductResponse>builder()
                        .code(200)
                        .message("Product Deleted")
                        .build()
        );
    }

    @PutMapping("/lock")
    ResponseEntity<ApiResponse<Boolean>> lock(@RequestBody @Valid LockProductRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<Boolean>builder()
                        .data(productService.lock(request))
                        .build()
        );
    }

    @PostMapping("/search")
    ResponseEntity<ApiResponse<List<ProductResponse>>> search(@RequestBody @Valid ProductFilter ids){
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<ProductResponse>>builder()
                        .data(productService.findByIds(ids))
                        .build()
        );
    }
}
