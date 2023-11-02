package com.segurosbolivar.sanyuschedulingapp.exception;

public class ScheduleExtensionExceptionMessage {

    public static final String SCHEDULE_EXTENSION_NOT_FOUND = "Schedule extension not found.";
    public static final String SCHEDULE_EXTENSION_ALREADY_EXISTS = "A schedule extension already exists for the specified work shift.";
    public static final String INVALID_TIME_FORMAT = "The time must be in HH:mm format.";
    public static final String INVALID_END_TIME = "The end time must be greater than the start time.";
    public static final String INVALID_TIME_RANGE = "Assigning a schedule extension within the work shift schedule is not possible.";

    public static String getScheduleExtensionNotFound() {
        return SCHEDULE_EXTENSION_NOT_FOUND;
    }

    public static String getScheduleExtensionAlreadyExists() {
        return SCHEDULE_EXTENSION_ALREADY_EXISTS;
    }

    public static String getInvalidTimeFormat() {
        return INVALID_TIME_FORMAT;
    }

    public static String getInvalidEndTime() {
        return INVALID_END_TIME;
    }

    public static String getInvalidTimeRange() {
        return INVALID_TIME_RANGE;
    }

}