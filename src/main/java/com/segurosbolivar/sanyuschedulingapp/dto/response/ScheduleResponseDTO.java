package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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