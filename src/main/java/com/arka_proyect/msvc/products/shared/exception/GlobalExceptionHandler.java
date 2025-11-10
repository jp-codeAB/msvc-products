package com.arka_proyect.msvc.products.shared.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException; // Necesario para 404 de ruta

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // **********************************
    // 404 NOT FOUND
    // **********************************

    // 404 - Para entidades de negocio no encontradas (ej. Producto ID: 5)
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleNotFoundExceptions(ProductNotFoundException ex) {
        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 404 - Para rutas que Spring no puede mapear (ej. /products/extra/5)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Endpoint Not Found")
                .message("The requested endpoint " + ex.getRequestURL() + " does not exist.")
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // **********************************
    // 400 BAD REQUEST & 409 CONFLICT
    // **********************************

    // 409 CONFLICT - Lógica de negocio (ej. Stock insuficiente, producto duplicado)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Business Logic Error")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorMessage> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        // Asegúrate de que tienes una clase ErrorMessage o similar para el cuerpo de la respuesta.
        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Duplicate Resource Conflict")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 400 BAD REQUEST - Errores de validación @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        // Devolvemos el mapa detallado de errores
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 400 BAD REQUEST - JSON malformado o tipo de dato incorrecto
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Malformed JSON Request")
                .message("The request body is malformed or invalid. Check JSON structure or data types.")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 400 BAD REQUEST - Faltan encabezados (ej. los de seguridad si no están bien configurados)
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Missing Required Header")
                .message("The required header '" + ex.getHeaderName() + "' is missing.")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // **********************************
    // 500 INTERNAL SERVER ERROR (CATCH-ALL)
    // **********************************

    /**
     * Captura cualquier excepción no controlada (como LazyInitializationException o NullPointerException)
     * y registra el error COMPLETO para debugging, pero envía un mensaje genérico al cliente.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> handleAllUncaughtException(Exception ex) {
        // Loguea el stack trace COMPLETO para ti
        log.error("ERROR INTERNO DEL SERVIDOR NO CONTROLADO: {}", ex.getMessage(), ex);

        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("Ha ocurrido un error inesperado en el servidor. Por favor, contacte con soporte.")
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}