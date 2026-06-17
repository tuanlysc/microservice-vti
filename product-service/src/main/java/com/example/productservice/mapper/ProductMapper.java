package com.example.productservice.mapper;

import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductUpdateRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse (Product product);

    Product toProduct(ProductCreateRequest request);

    @Mapping(target = "id", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);

}
