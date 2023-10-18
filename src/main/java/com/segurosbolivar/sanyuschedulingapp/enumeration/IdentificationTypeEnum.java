package com.segurosbolivar.sanyuschedulingapp.enumeration;

import lombok.Getter;

@Getter
public enum IdentificationTypeEnum {

    CC("Cédula de Ciudadanía"),
    CE("Cédula de Extranjería");

    private final String description;

    IdentificationTypeEnum(String description) {
        this.description = description;
    }

}