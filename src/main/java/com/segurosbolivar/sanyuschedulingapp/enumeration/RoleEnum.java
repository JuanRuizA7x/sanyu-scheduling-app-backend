package com.segurosbolivar.sanyuschedulingapp.enumeration;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ADMINISTRATOR("Administrador"),
    CONTRACTOR("Contratista");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

}