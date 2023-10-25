package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Long workShiftId;
    @NotNull
    private LocalDateTime date;
    @NotNull
    private ScheduleResponseDTO schedule;
    @NotNull
    private UserResponseDTO user;
    @NotNull
    private Boolean isStarted;
    private LocalDateTime startedAt;

}