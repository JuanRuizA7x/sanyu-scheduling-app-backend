package com.segurosbolivar.sanyuschedulingapp.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CsvFileErrorResponseDTO {

    private Integer row;
    private String message;

}