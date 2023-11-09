package com.segurosbolivar.sanyuschedulingapp.util;

import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftException;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftExceptionMessage;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateValidator {

    /**
     * Validates a date range to ensure the start date is in the future and before the end date.
     *
     * @param startDate The start date of the range to validate.
     * @param endDate   The end date of the range to validate.
     * @throws WorkShiftException if the date range is invalid, such as if the start date is in the past or if the end date is before the start date.
     */
    public static void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {

        Date startDateSQL = Date.valueOf(startDate.toLocalDate());
        Date endDateSQL = Date.valueOf(endDate.toLocalDate());
        Date currentDate = Date.valueOf(LocalDate.now());

        int startDateValidation = startDateSQL.compareTo(currentDate);
        int endDateValidation = endDateSQL.compareTo(startDateSQL);

        if(startDateValidation < 1) {
            throw new WorkShiftException(
                    WorkShiftExceptionMessage.getInvalidStartDate(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(endDateValidation < 0) {
            throw new WorkShiftException(
                    WorkShiftExceptionMessage.getInvalidEndDate(),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

}