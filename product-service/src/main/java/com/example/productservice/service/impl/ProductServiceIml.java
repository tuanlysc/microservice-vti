package com.example.productservice.service.impl;

import com.example.productservice.dto.request.*;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.event.LockResultEvent;
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
import org.redisson.api.RedissonClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceIml implements ProductService {

    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ProductMapper productMapper;
    KafkaTemplate<String, LockResultEvent>  kafkaTemplate;
    RedissonClient redissonClient;

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public List<ProductResponse> findByIds(ProductFilter request) {

        List<String> ids = request.getProductIds().stream().toList();

        List<Product> products = productRepository.findByIdIn(ids);

        return products
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

    @Override
    @Transactional
    public Boolean lock(LockProductRequest request) {
        List<LockProductItemRequest>  items = request.getItems();
        List<String> productIds = items.stream().map(LockProductItemRequest::getProductId).toList();

        Map<String,Integer> productsMap = items.stream()
                .collect(Collectors.toMap(LockProductItemRequest::getProductId, LockProductItemRequest::getQuantity));

        List<Product> products = productRepository.findByIdInForUpdate(productIds);

        boolean success = true;
        String failReason = null;
        List<Product> productStock = new ArrayList<>();
        for (Product product : products) {
            int newStock = product.getStock() - productsMap.get(product.getId());
            if (newStock < 0) {
                success = false;
                failReason = "STOCK TO LOW" + product.getId();
                break;
            }
            product.setStock(newStock);
            productStock.add(product);
        }

        if (!success) {
            sendLockKafka(request.getOrderId(), success, failReason);

            return false;
        }

        sendLockKafka(request.getOrderId(), success, failReason);
        productRepository.saveAll(productStock);

        return true;
    }

    private void sendLockKafka(String orderId, Boolean isSuccess, String failReason) {
        LockResultEvent lockResultEvent = new LockResultEvent();
        lockResultEvent.setOrderId(orderId);
        lockResultEvent.setIsSuccess(isSuccess);
        lockResultEvent.setFailReason(failReason);
        kafkaTemplate.send("product.lock.result",lockResultEvent);
    }

}
