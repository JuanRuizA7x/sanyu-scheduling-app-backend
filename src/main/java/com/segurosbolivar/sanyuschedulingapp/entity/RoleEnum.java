package com.segurosbolivar.sanyuschedulingapp.entity;

public enum RoleEnum {

    ADMINISTRATOR("Administrador"),
    CONTRACTOR("Contratista");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}