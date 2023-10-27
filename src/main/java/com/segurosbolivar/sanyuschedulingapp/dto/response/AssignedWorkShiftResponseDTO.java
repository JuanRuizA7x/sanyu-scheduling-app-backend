package com.segurosbolivar.sanyuschedulingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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