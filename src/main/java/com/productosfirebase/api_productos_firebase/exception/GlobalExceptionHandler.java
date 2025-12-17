package com.productosfirebase.api_productos_firebase.exception;

import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<Map<String,Object>>> handleNotFound(ResourceNotFoundException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error","Not Found","message",ex.getMessage())));
    }

  @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<Map<String,Object>>> handleValidation(MethodArgumentNotValidException ex) {
        var details = ex.getBindingResult().getFieldErrors().stream().map(fe -> Map.of("field", fe.getField(), "message", fe.getDefaultMessage())).toList();
        
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","Bad Request","message","Validación fallida","details",details)));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String,Object>>> handleGeneric(Exception ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Internal Server Error","message",ex.getMessage())));
    }
}
