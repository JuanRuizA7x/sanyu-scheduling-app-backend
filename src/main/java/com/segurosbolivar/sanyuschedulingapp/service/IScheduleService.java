package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.ScheduleResponseDTO;

import java.util.List;

public interface IScheduleService {

    List<ScheduleResponseDTO> findAll();
    ScheduleResponseDTO findByScheduleId(Long scheduleId);

}