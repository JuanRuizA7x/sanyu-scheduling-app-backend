package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDTO {

    @NotNull
    private Long scheduleId;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(min = 5, max = 5)
    private String startTime;
    @NotBlank
    @Size(min = 5, max = 5)
    private String endTime;
    @Size(min = 5, max = 5)
    private String breakStartTime;
    @Size(min = 5, max = 5)
    private String breakEndTime;

}