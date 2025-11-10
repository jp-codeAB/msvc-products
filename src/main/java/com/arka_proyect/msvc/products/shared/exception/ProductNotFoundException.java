package com.arka_proyect.msvc.products.shared.exception;

public class ProductNotFoundException extends RuntimeException {

    // Constructor que acepta un mensaje (String)
    public ProductNotFoundException(String message) {
        super(message);
    }

    // Opcional: Constructor que acepta mensaje y causa
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}