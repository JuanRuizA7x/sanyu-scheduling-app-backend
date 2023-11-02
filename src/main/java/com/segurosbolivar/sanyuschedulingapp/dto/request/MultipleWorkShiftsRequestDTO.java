package com.segurosbolivar.sanyuschedulingapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MultipleWorkShiftsRequestDTO {

    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    private Long scheduleId;
    @NotNull
    private Long userId;

}