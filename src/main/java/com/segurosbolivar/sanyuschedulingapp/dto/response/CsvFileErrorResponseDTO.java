package com.segurosbolivar.sanyuschedulingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CsvFileErrorResponseDTO {

    private Integer row;
    private String message;

}