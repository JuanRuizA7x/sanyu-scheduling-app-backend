package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentificationTypeResponseDTO {

    @NotNull
    private Long identificationTypeId;
    @NotBlank
    @Size(max = 50)
    private String code;
    @NotBlank
    @Size(max = 50)
    private String name;
    @Size(max = 255)
    private String description;

}