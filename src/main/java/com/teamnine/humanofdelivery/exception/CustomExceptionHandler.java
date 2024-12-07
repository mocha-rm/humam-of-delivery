package com.teamnine.humanofdelivery.exception;

import com.teamnine.humanofdelivery.exception.order.OrderException;
import com.teamnine.humanofdelivery.exception.store.StoreException;
import com.teamnine.humanofdelivery.exception.user.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserException.class)

    public ResponseEntity<Map<String, Object>> handleCustomException(UserException ex) {
        return getMapResponseEntity("error", ex.getMessage(), ex.getUserErrorCode().getHttpStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return getMapResponseEntity("error", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StoreException.class)
    public ResponseEntity<Map<String, Object>> handleStoreException(StoreException ex) {
        return getMapResponseEntity(ex.getStoreErrorCode().toString(), ex.getMessage(), ex.getStoreErrorCode().getHttpStatus());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Map<String, Object>> handleOrderException(OrderException ex) {
        return getMapResponseEntity(ex.getOrderErrorCode().toString(), ex.getMessage(), ex.getOrderErrorCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(fieldErrors);
    }

    private static ResponseEntity<Map<String, Object>> getMapResponseEntity(String errorName, String errorMessage, HttpStatus httpStatus) {
        Map<String, Object> response = new HashMap<>();
        response.put(errorName, errorMessage);
        response.put("status", httpStatus);

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}