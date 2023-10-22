package com.segurosbolivar.sanyuschedulingapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> taskExceptionHandler(UserException userException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", userException.getMessage());
        return new ResponseEntity<Map<String, String>>(response, userException.getHttpStatus());
    }

}