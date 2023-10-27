package com.segurosbolivar.sanyuschedulingapp.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkShiftReportResponseDTO {

    private List<AssignedWorkShiftResponseDTO> assignedWorkShiftResponseDTOList;
    private String errorMessage;

}