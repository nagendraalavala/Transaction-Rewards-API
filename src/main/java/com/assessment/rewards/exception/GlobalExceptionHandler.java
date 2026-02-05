package com.assessment.rewards.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final String TIMESTAMP = "timeStamp";
    private static final String ERROR = "error";


    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                TIMESTAMP, LocalDateTime.now(),
                ERROR, ex.getMessage()
        ));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put(TIMESTAMP, LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put(ERROR, "Validation Failed");

        String errorMessage = Optional.ofNullable(ex.getBindingResult())
                .map(org.springframework.validation.BindingResult::getFieldError)
                .map(org.springframework.validation.FieldError::getDefaultMessage)
                .orElse("Validation error");

        error.put("message", errorMessage);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put(TIMESTAMP, LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put(ERROR, "Constraint Violation");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.internalServerError().body(Map.of(
                TIMESTAMP, LocalDateTime.now(),
                ERROR, "Internal server error",
                "details", ex.getMessage()
        ));
    }
}
