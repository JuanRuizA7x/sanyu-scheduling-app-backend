package com.segurosbolivar.sanyuschedulingapp.enumeration;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ADMINISTRATOR("Administrador"),
    SUPERVISOR_CONTRACTOR("Contratista Supervisor"),
    FIELD_CONTRACTOR("Contratista de Campo");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

}