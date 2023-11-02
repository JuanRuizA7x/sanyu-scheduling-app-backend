package com.segurosbolivar.sanyuschedulingapp.service;

import java.time.LocalDateTime;

public interface IWorkShiftReportService {

    void generateAutomaticReport();
    void generateManualReport(String toEmail, LocalDateTime date);

}