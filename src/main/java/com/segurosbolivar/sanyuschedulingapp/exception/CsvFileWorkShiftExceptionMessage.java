package com.segurosbolivar.sanyuschedulingapp.exception;

public class CsvFileWorkShiftExceptionMessage {

    public static String INVALID_FILE_EXTENSION = "The file extension must be \".csv\".";
    public static String USER_NOT_FOUND = "No existe un contratista con identificación ${identification}.";
    public static String SCHEDULE_NOT_FOUND = "No existe un horario con nombre ${scheduleName}.";
    public static String INVALID_DATE_FORMAT = "La fecha debe ser en el formato \"yyyy-mm-dd\".";
    public static String INVALID_START_DATE = "La fecha de inicio no puede ser menor a la fecha actual.";
    public static String INVALID_END_DATE = "La fecha de finalización no puede ser menor a la fecha de inicio.";
    public static String INVALID_ROLE_ASSIGNMENT = "Solo a los usuarios con cualquier rol de contratista se les pueden asignar turnos de trabajo.";
    public static String INVALID_SCHEDULE_ASSIGNMENT = "El horario ${scheduleName} no se le puede asignar a un usuario con el rol de ${roleName}.";

    public static String getInvalidFileExtension() {
        return INVALID_FILE_EXTENSION;
    }

    public static String getUserNotFound(String identification) {
        return USER_NOT_FOUND.replace("${identification}", identification);
    }

    public static String getScheduleNotFound(String scheduleName) {
        return SCHEDULE_NOT_FOUND.replace("${scheduleName}", scheduleName);
    }

    public static String getInvalidDateFormat() {
        return INVALID_DATE_FORMAT;
    }

    public static String getInvalidStartDate() {
        return INVALID_START_DATE;
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

}