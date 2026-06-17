package com.example.productservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CATEGORY_NOT_FOUND(400, "Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(400, "Product Not Found", HttpStatus.NOT_FOUND),
    ROLE_INVALID(400, "Role Is Valid", HttpStatus.BAD_REQUEST),
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
