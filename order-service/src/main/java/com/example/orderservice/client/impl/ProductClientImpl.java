package com.example.orderservice.client.impl;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.dto.reponse.ProductResponse;
import com.example.orderservice.client.dto.request.LockProductRequest;
import com.example.orderservice.client.dto.request.ProductFilter;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductClientImpl implements ProductClient {

    WebClient webClient;

    public ProductClientImpl(@Qualifier("productWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public List<ProductResponse> search(ProductFilter productFilter) {

        ApiResponse<List<ProductResponse>> response =
                webClient.post()
                        .uri("/product/search")
                        .bodyValue(productFilter)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<ProductResponse>>>() {})
                        .block();

        if (response == null || response.getData() == null) {
            throw new ApplicationException(ErrorCode.CALL_PRODUCT_ERROR);
        }
        return response.getData();
    }

    @Override
    public Boolean lock(LockProductRequest request) {

        ApiResponse<Boolean> response =
                webClient.put()
                        .uri("/product/lock")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<ApiResponse<Boolean>>() {})
                        .block();

        if (response == null || response.getData() == null) {
            throw new ApplicationException(ErrorCode.CALL_PRODUCT_ERROR);
        }
        return response.getData();
    }
}
