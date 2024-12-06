package com.teamnine.humanofdelivery.exception;

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
    public ResponseEntity<Map<String, String>> handleCustomException(UserException ex) {

        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());

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
        Map<String, String> response = new HashMap<>();
        response.put(ex.getStoreErrorCode().toString(), ex.getMessage());

        return ResponseEntity
                .status(ex.getStoreErrorCode().getHttpStatus())
                .body(response);
    }
}
