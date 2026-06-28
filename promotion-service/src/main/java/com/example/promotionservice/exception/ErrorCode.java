package com.example.promotionservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    PROMOTION_NOT_FOUND(400, "Promotion not found", HttpStatus.NOT_FOUND),
    PROMOTION_INACTIVE(400, "Promotion inactive", HttpStatus.NOT_FOUND),
    PROMOTION_NOT_STARTED(400, "Promotion not started", HttpStatus.BAD_REQUEST),
    PROMOTION_EXPIRED(400, "Promotion expired", HttpStatus.BAD_REQUEST),
    ORDER_VALUE_TOO_LOW(400, "Order value too low", HttpStatus.BAD_REQUEST),
    PROMOTION_USAGE_LIMIT_REACHED(400, "Promotion usage limit reached", HttpStatus.BAD_REQUEST),
    PROMOTION_ALREADY_USED_FOR_ORDER(400, "Promotion already used for order", HttpStatus.BAD_REQUEST),
    PROMOTION_USAGE_NOT_FOUND(400, "Promotion usage not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    PROMOTION_CODE_EXITED(400, "Promotion code exited", HttpStatus.NOT_FOUND),
    PROMOTION_CODE_NOT_FOUND(400, "Promotion code not found", HttpStatus.NOT_FOUND),
    PROMOTION_INVALID_DATE_RANGE(400,"Promotion invalid date range" , HttpStatus.BAD_REQUEST ),
    PROMOTION_END_DATE_MUST_BE_FUTURE(400,"Promotion end date must be future" , HttpStatus.BAD_REQUEST ),;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
