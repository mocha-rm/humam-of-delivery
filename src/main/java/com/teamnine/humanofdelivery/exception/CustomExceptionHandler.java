package com.teamnine.humanofdelivery.exception;

import com.teamnine.humanofdelivery.exception.order.OrderException;
import com.teamnine.humanofdelivery.exception.store.StoreException;
import com.teamnine.humanofdelivery.exception.user.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> handleUserException(UserException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("code", ex.getErrorCode().getCode());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(StoreException.class)
    public ResponseEntity<Map<String, String>> handleStoreException(StoreException ex) {
        return getMapResponseEntity(ex.getStoreErrorCode().toString(), ex.getMessage(), ex.getStoreErrorCode().getHttpStatus());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Map<String, String>> handleOrderException(OrderException ex) {
        return getMapResponseEntity(ex.getOrderErrorCode().toString(), ex.getMessage(), ex.getOrderErrorCode().getHttpStatus());
    }



    private static ResponseEntity<Map<String, String>> getMapResponseEntity(String errorName, String errorMessage, HttpStatus httpStatus) {
        Map<String, String> response = new HashMap<>();
        response.put(errorName, errorMessage);

        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
}
