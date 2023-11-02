package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.request.ScheduleExtensionRequestDTO;

public interface IScheduleExtensionService {

    void extendSchedule(ScheduleExtensionRequestDTO scheduleExtensionRequestDTO);

}