package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftReportResponseDTO;

import java.time.LocalDateTime;


public interface IWorkShiftReportService {

    WorkShiftReportResponseDTO getAssignedWorkShiftsByDate(LocalDateTime date);

}