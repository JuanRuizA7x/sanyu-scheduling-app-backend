package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.request.CsvFileWorkShiftRequestDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CsvRowToCsvFileWorkShiftRequestDTOMapper implements IMapper<String [], CsvFileWorkShiftRequestDTO> {

    @Override
    public CsvFileWorkShiftRequestDTO map(String[] csvRow) {
        return CsvFileWorkShiftRequestDTO.builder()
                .identificationType(csvRow[0].trim())
                .identificationNumber(csvRow[1].trim())
                .schedule(csvRow[2].trim())
                .startDate(csvRow[3].trim())
                .endDate(csvRow[4].trim())
                .build();
    }

    @Override
    public List<CsvFileWorkShiftRequestDTO> map(List<String[]> csvRowList) {

        List<CsvFileWorkShiftRequestDTO> response = new ArrayList<>();

        csvRowList.forEach(csvRow -> {
            response.add(
                    CsvFileWorkShiftRequestDTO.builder()
                            .identificationType(csvRow[0].trim())
                            .identificationNumber(csvRow[1].trim())
                            .schedule(csvRow[2].trim())
                            .startDate(csvRow[3].trim())
                            .endDate(csvRow[4].trim())
                            .build()
            );
        });

        return response;
    }

}