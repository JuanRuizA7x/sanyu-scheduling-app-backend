package com.segurosbolivar.sanyuschedulingapp.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HolidayResponseDTO {

    @NotBlank
    //'yyyy-mm-dd'
    private String date;
    private String localName;
    private String name;
    private String countryCode;

}