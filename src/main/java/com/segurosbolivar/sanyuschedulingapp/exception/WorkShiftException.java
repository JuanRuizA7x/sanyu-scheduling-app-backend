package com.segurosbolivar.sanyuschedulingapp.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class WorkShiftException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;

    public WorkShiftException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}