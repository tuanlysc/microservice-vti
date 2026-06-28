package com.example.productservice.service.impl;

import com.example.productservice.dto.request.CategoryCreateRequest;
import com.example.productservice.dto.request.CategoryUpdateRequest;
import com.example.productservice.dto.response.CategoryResponse;
import com.example.productservice.exception.ApplicationException;
import com.example.productservice.exception.ErrorCode;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceIml implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if(request.getParentId()!= null && !categoryRepository.existsById(request.getParentId())){
            throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND);
        }else if(categoryRepository.findCategoryByName(request.getName()).isPresent()){
            throw new ApplicationException(ErrorCode.CATEGORY_DUPLICATE_NAME);
        }
        var category = categoryMapper.toCategory(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse findById(String id) {
        return null;
    }

    @Override
    public CategoryResponse update(String id, CategoryUpdateRequest request) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
