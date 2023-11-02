package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleResponseDTO {

    @NotNull
    private Long roleId;
    @NotBlank
    @Size(max = 50)
    private String name;
    @Size(max = 255)
    private String description;

}