package com.example.productservice.service;

import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductFilter;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductCreateRequest request);

    List<ProductResponse> findAllByCategoryId(String categoryId);

    List<ProductResponse> findAll();

    List<ProductResponse> findByName(String productName);

    List<ProductResponse> findByIds(ProductFilter request);

    ProductResponse update(String id, ProductUpdateRequest request);

    void deleteProductById(String id);
}
