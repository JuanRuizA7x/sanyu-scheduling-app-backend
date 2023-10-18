package com.segurosbolivar.sanyuschedulingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDTO {

    private Long scheduleId;
    private String name;
    private String startTime;
    private String endTime;
    private String breakStartTime;
    private String breakEndTime;

}