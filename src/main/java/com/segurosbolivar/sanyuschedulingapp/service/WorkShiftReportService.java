package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftReportResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftException;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.repository.WorkShiftJDBCRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class WorkShiftReportService implements IWorkShiftReportService {

    private final WorkShiftJDBCRepository workShiftJDBCRepository;

    public WorkShiftReportService(WorkShiftJDBCRepository workShiftJDBCRepository) {
        this.workShiftJDBCRepository = workShiftJDBCRepository;
    }

    @Override
    public WorkShiftReportResponseDTO getAssignedWorkShiftsByDate(LocalDateTime date) {

        WorkShiftReportResponseDTO response = null;

        try {
            response = this.workShiftJDBCRepository.getAssignedWorkShiftsByDate(
                    Date.valueOf(date.toLocalDate())
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(response.getErrorMessage() != null && !response.getErrorMessage().isBlank()) {
            throw new WorkShiftException(
                    WorkShiftExceptionMessage.getWorkShiftReportError(response.getErrorMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return response;

    }

}