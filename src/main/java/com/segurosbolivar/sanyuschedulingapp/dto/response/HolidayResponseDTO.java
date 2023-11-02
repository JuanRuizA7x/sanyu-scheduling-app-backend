package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HolidayResponseDTO {

    @NotBlank
    private String date;
    private String localName;
    private String name;
    private String countryCode;

}