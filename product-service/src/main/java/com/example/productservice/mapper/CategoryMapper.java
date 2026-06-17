package com.example.productservice.mapper;

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.request.CategoryUpdateRequest;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);

    Category toCategory(CategoryCreateRequest request);

    void update(@MappingTarget Category category, CategoryUpdateRequest request);
}
