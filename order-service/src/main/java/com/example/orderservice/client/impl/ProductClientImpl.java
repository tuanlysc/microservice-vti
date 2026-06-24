package com.example.orderservice.client.impl;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.dto.reponse.ProductResponse;
import com.example.orderservice.client.dto.request.ProductFilter;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductClientImpl implements ProductClient {

    WebClient webClient;

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
}
