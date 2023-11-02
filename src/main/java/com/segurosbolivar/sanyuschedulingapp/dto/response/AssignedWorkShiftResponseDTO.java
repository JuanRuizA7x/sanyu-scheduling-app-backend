package com.segurosbolivar.sanyuschedulingapp.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AssignedWorkShiftResponseDTO {

    private String fullName;
    private String identificationType;
    private String identificationNumber;
    private String email;
    private String role;
    private String schedule;
    private String startTime;
    private String endTime;
    private String isStarted;
    private String extensionStartTime;
    private String extensionEndTime;
    private String extensionReason;

}