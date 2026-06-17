package com.example.productservice.service.iml;

import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.ApplicationException;
import com.example.productservice.exception.ErrorCode;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.repository.CategoryRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceIml implements ProductService {

    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override
    public ProductResponse create(ProductCreateRequest request) {
        Product product = productMapper.toProduct(request);
        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        product = productRepository.save(product);

        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> findAllByCategoryId(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return productRepository.findAllByCategoryId(categoryId)
                .stream()
                .filter(product -> !product.getIsDeleted())
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> findByName(String productName) {
        return productRepository.findByProductName(productName)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse update(String id, ProductUpdateRequest request) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new ApplicationException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        productMapper.updateProduct(product, request);

        return productMapper.toResponse(product);
    }

    @Override
    public void deleteProductById(String id) {
        var product = productRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        product.setIsDeleted(true);

        productRepository.save(product);
    }

}
