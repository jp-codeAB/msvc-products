package com.arka_proyect.msvc.products.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessException extends RuntimeException {

    private final HttpStatus status;


    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT; // 409
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}