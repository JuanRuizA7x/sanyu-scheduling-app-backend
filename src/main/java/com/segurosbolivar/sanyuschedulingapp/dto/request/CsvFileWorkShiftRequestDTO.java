package com.segurosbolivar.sanyuschedulingapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CsvFileWorkShiftRequestDTO {

    @NotNull
    private String identificationType;
    @NotNull
    private String identificationNumber;
    @NotNull
    private String schedule;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;

}