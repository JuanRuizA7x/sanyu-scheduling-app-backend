package com.segurosbolivar.sanyuschedulingapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkShiftResponseDTO {

    private Long workShiftId;
    private LocalDateTime date;
    private ScheduleResponseDTO schedule;
    private UserResponseDTO user;
    private Boolean isStarted;
    private LocalDateTime startedAt;

}