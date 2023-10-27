package com.segurosbolivar.sanyuschedulingapp.exception;

public class WorkShiftExceptionMessage {

    public static String WORK_SHIFT_NOT_FOUND = "Work shift not found.";
    public static String INVALID_DATE = "The entered date cannot be earlier than or equal to the current date.";
    public static String INVALID_END_DATE = "The end date cannot be earlier than the start date.";
    public static String INVALID_ROLE_ASSIGNMENT = "Only users with any contractor role can be assigned work shifts.";
    public static String INVALID_SCHEDULE_ASSIGNMENT = "The ${scheduleName} schedule cannot be assigned to a user with the ${roleName} role.";
    public static String INVALID_DATE_RANGE = "The contractor already has work shifts assigned within the specified date range.";
    public static String WORK_SHIFT_REPORT_ERROR = """
            An error occurred during the report generation.
            
            Details:
            
            ${errorMessage}
            """;

    public static String getWorkShiftNotFound() {
        return WORK_SHIFT_NOT_FOUND;
    }

    public static String getInvalidDate() {
        return INVALID_DATE;
    }

    public static String getInvalidEndDate() {
        return INVALID_END_DATE;
    }

    public static String getInvalidRoleAssignment() {
        return INVALID_ROLE_ASSIGNMENT;
    }

    public static String getInvalidScheduleAssignment(String scheduleName, String roleName) {
        return INVALID_SCHEDULE_ASSIGNMENT
                .replace("${scheduleName}", scheduleName)
                .replace("${roleName}", roleName);
    }

    public static String getInvalidDateRange() {
        return INVALID_DATE_RANGE;
    }

    public static String getWorkShiftReportError(String errorMessage) {
        return WORK_SHIFT_REPORT_ERROR
                .replace("${errorMessage}", errorMessage);
    }

}