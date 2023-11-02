package com.segurosbolivar.sanyuschedulingapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScheduleExtensionRequestDTO {

    @NotBlank
    @Size(min = 5, max = 5)
    private String startTime;
    @NotBlank
    @Size(min = 5, max = 5)
    private String endTime;
    @NotBlank
    private String reason;
    @NotNull
    private Long workShiftId;

}