package com.segurosbolivar.sanyuschedulingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle exceptions of type IdentificationTypeException.
     *
     * @param identificationTypeException The exception to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(IdentificationTypeException.class)
    public ResponseEntity<Map<String, String>> identificationTypeExceptionHandler(IdentificationTypeException identificationTypeException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", identificationTypeException.getMessage());
        return new ResponseEntity<Map<String, String>>(response, identificationTypeException.getHttpStatus());
    }

    /**
     * Handle exceptions of type RoleException.
     *
     * @param roleException The exception to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(RoleException.class)
    public ResponseEntity<Map<String, String>> roleExceptionHandler(RoleException roleException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", roleException.getMessage());
        return new ResponseEntity<Map<String, String>>(response, roleException.getHttpStatus());
    }

    /**
     * Handle exceptions of type ScheduleException.
     *
     * @param scheduleException The exception to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(ScheduleException.class)
    public ResponseEntity<Map<String, String>> scheduleExceptionHandler(ScheduleException scheduleException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", scheduleException.getMessage());
        return new ResponseEntity<Map<String, String>>(response, scheduleException.getHttpStatus());
    }

    /**
     * Handle exceptions of type ScheduleExtensionException.
     *
     * @param scheduleExtensionException The exception to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(ScheduleExtensionException.class)
    public ResponseEntity<Map<String, String>> scheduleExtensionExceptionHandler(ScheduleExtensionException scheduleExtensionException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", scheduleExtensionException.getMessage());
        return new ResponseEntity<Map<String, String>>(response, scheduleExtensionException.getHttpStatus());
    }

    /**
     * Handle exceptions of type UserException.
     *
     * @param userException The exception to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> userExceptionHandler(UserException userException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", userException.getMessage());
        return new ResponseEntity<Map<String, String>>(response, userException.getHttpStatus());
    }

    /**
     * Handle exceptions of type WorkShiftException.
     *
     * @param workShiftException The exception to handle.
     * @return A ResponseEntity with an error message and HTTP status code.
     */
    @ExceptionHandler(WorkShiftException.class)
    public ResponseEntity<Map<String, String>> workShiftExceptionHandler(WorkShiftException workShiftException) {
        Map<String, String> response = new HashMap<>();
        response.put("message", workShiftException.getMessage());
        return new ResponseEntity<Map<String, String>>(response, workShiftException.getHttpStatus());
    }

    /**
     * Handle general exceptions of type Exception.
     *
     * @param exception The general exception to handle.
     * @return A ResponseEntity with an error message and the HTTP status code set to INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(Exception exception) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exception.getMessage());
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}