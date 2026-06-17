package com.example.productservice.service;

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.request.CategoryUpdateRequest;
import com.example.productservice.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest request);

    List<CategoryResponse> findAll();

    CategoryResponse findById(String id);

    CategoryResponse update(String id, CategoryUpdateRequest request);

    void deleteById(String id);
}
