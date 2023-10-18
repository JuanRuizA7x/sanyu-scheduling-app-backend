package com.segurosbolivar.sanyuschedulingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentificationTypeResponseDTO {

    private Long identificationTypeId;
    private String code;
    private String name;
    private String description;

}