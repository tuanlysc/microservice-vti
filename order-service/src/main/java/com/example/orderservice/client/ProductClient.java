package com.example.orderservice.client;

import com.example.orderservice.client.dto.reponse.ProductResponse;
import com.example.orderservice.client.dto.request.ProductFilter;

import java.util.List;

public interface ProductClient {
    List<ProductResponse> search(ProductFilter productFilter);
}
