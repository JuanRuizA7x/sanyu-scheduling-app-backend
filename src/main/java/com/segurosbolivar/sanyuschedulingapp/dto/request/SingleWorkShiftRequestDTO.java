package com.segurosbolivar.sanyuschedulingapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SingleWorkShiftRequestDTO {

    @NotNull
    private LocalDateTime date;
    @NotNull
    private Long scheduleId;
    @NotNull
    private Long userId;

}