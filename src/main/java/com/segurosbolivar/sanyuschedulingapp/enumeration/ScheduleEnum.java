package com.segurosbolivar.sanyuschedulingapp.enumeration;

import lombok.Getter;

@Getter
public enum ScheduleEnum {

    MORNING("Mañana"),
    AFTERNOON("Tarde"),
    OFFICE("Oficina");

    private final String description;

    ScheduleEnum(String description) {
        this.description = description;
    }

}