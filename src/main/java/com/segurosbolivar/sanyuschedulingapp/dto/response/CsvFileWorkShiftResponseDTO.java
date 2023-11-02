package com.segurosbolivar.sanyuschedulingapp.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CsvFileWorkShiftResponseDTO {

    private Integer insertsCount;
    private Integer updatesCount;
    private List<CsvFileErrorResponseDTO> errors;

    public void incrementInsertsCount(int count) {
        this.insertsCount += count;
    }

    public void incrementUpdatesCount(int count) {
        this.updatesCount += count;
    }

    public void addError(CsvFileErrorResponseDTO error) {
        this.errors.add(error);
    }

}