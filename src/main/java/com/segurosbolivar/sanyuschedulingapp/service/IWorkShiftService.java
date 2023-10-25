package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.request.MultipleWorkShiftsRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.request.SingleWorkShiftRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IWorkShiftService {

    List<WorkShiftResponseDTO> findAllByUserIdAndStartDate(Long userId, LocalDateTime startDate);
    WorkShiftResponseDTO findByWorkShiftId(Long workShiftId);
    int assignMultipleWorkShifts(MultipleWorkShiftsRequestDTO multipleWorkShiftsRequestDTO);
    int assignSingleWorkShift(SingleWorkShiftRequestDTO singleWorkShiftRequestDTO, List<String> holidayResponseDTOList);

}