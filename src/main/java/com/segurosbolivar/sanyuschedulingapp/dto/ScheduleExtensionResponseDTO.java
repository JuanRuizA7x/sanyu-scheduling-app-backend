package com.segurosbolivar.sanyuschedulingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleExtensionResponseDTO {

    private Long scheduleExtensionId;
    private String startTime;
    private String endTime;
    private String reason;
    private WorkShiftResponseDTO workShift;

}