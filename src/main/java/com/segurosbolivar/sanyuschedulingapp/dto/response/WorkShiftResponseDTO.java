package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkShiftResponseDTO {

    @NotNull
    private Long workShiftId;
    @NotNull
    private LocalDateTime date;
    @NotNull
    private ScheduleResponseDTO schedule;
    @NotNull
    private Long userId;
    private List<ScheduleExtensionResponseDTO> scheduleExtensions;
    @NotNull
    private Boolean isStarted;
    private LocalDateTime startedAt;

}