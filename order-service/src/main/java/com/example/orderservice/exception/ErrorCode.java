package com.example.orderservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CATEGORY_NOT_FOUND(400, "Category not found", HttpStatus.NOT_FOUND),
    CALL_PRODUCT_ERROR(404, "Call product error", HttpStatus.NOT_FOUND),
    DUPLICATE_KEY_PRODUCT(400, "Duplicate key product", HttpStatus.BAD_REQUEST),
    CATEGORY_DUPLICATE_NAME(400, "Category Duplicate Name", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
