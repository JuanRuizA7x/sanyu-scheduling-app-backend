package com.segurosbolivar.sanyuschedulingapp.dto.request;

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
public class SingleWorkShiftRequestDTO {

    @NotNull
    private LocalDateTime date;
    @NotNull
    private Long scheduleId;
    @NotNull
    private Long userId;

}