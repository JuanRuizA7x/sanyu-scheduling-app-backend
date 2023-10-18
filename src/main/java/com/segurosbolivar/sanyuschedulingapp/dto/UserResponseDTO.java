package com.segurosbolivar.sanyuschedulingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String secondLastName;
    private IdentificationTypeResponseDTO identificationType;
    private String identificationNumber;
    private String email;
    private RoleResponseDTO role;

}