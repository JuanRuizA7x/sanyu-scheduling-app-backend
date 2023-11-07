package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.request.MultipleWorkShiftsRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IWorkShiftService {

    int assignMultipleWorkShifts(MultipleWorkShiftsRequestDTO multipleWorkShiftsRequestDTO);
    List<WorkShiftResponseDTO> findByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    List<WorkShiftResponseDTO> findByEmailAndDateRange(String email, LocalDateTime startDate, LocalDateTime endDate);
    void markWorkShiftAsStarted(Long workShiftId);

}