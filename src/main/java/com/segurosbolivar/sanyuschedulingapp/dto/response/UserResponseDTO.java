package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.Email;
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
public class UserResponseDTO {

    @NotNull
    private Long userId;
    @NotBlank
    @Size(max = 50)
    private String firstName;
    @Size(max = 50)
    private String middleName;
    @NotBlank
    @Size(max = 50)
    private String lastName;
    @Size(max = 50)
    private String secondLastName;
    @NotBlank
    private IdentificationTypeResponseDTO identificationType;
    @NotNull
    @Size(max = 20)
    private String identificationNumber;
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;
    @NotNull
    private RoleResponseDTO role;

}