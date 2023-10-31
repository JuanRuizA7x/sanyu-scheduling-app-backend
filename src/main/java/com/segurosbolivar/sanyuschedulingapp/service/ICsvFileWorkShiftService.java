package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.CsvFileWorkShiftResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ICsvFileWorkShiftService {

    CsvFileWorkShiftResponseDTO processCsvFile(MultipartFile csvFile);

}