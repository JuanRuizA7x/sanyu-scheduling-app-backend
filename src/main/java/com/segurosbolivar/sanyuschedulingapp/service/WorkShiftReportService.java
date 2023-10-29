package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.HolidayResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftReportResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.RoleEntity;
import com.segurosbolivar.sanyuschedulingapp.enumeration.RoleEnum;
import com.segurosbolivar.sanyuschedulingapp.exception.RoleException;
import com.segurosbolivar.sanyuschedulingapp.exception.RoleExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftException;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.repository.IRoleRepository;
import com.segurosbolivar.sanyuschedulingapp.repository.IUserRepository;
import com.segurosbolivar.sanyuschedulingapp.repository.WorkShiftJDBCRepository;
import com.segurosbolivar.sanyuschedulingapp.service.client.HolidayService;
import com.segurosbolivar.sanyuschedulingapp.util.CsvGenerator;
import com.segurosbolivar.sanyuschedulingapp.service.client.MailSenderService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkShiftReportService implements IWorkShiftReportService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final WorkShiftJDBCRepository workShiftJDBCRepository;
    private final HolidayService holidayService;
    private final MailSenderService mailSenderService;

    private final String[] headers = {
            "NOMBRE COMPLETO",
            "TIPO DE IDENTIFICACIÓN",
            "NÚMERO DE IDENTIFICACIÓN",
            "EMAIL",
            "ROL",
            "HORARIO",
            "HORA DE INICIO",
            "HORA DE FINALIZACIÓN",
            "TURNO INICIADO",
            "HORA DE INICIO DE EXTENSIÓN DE JORNADA",
            "HORA DE FINALIZACIÓN DE EXTENSIÓN DE JORNADA",
            "RAZÓN DE EXTENSIÓN DE JORNADA"
    };

    private final String[] nameMapping = {
            "fullName",
            "identificationType",
            "identificationNumber",
            "email",
            "role",
            "schedule",
            "startTime",
            "endTime",
            "isStarted",
            "extensionStartTime",
            "extensionEndTime",
            "extensionReason"
    };

    public WorkShiftReportService(
            IUserRepository userRepository,
            IRoleRepository roleRepository,
            WorkShiftJDBCRepository workShiftJDBCRepository,
            HolidayService holidayService,
            MailSenderService mailSenderService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.workShiftJDBCRepository = workShiftJDBCRepository;
        this.holidayService = holidayService;
        this.mailSenderService = mailSenderService;
    }

    @Scheduled(cron = "0 0 0 ? * MON-FRI")
    @Override
    public void generateAutomaticReport() {

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<HolidayResponseDTO> holidaysList = this.holidayService.getHolidaysByYearAndCountryCode(
                LocalDateTime.now().getYear(), "CO"
        );
        boolean isBusinessDay = true;

        for (HolidayResponseDTO holidayResponseDTO : holidaysList) {

            if (currentDate.equals(holidayResponseDTO.getDate())) {
                isBusinessDay = false;
                break;
            }

        }

        if(isBusinessDay) {

            RoleEntity role = this.roleRepository.findByName(RoleEnum.ADMINISTRATOR.getDescription())
                    .orElseThrow(() -> new RoleException(
                            RoleExceptionMessage.getRoleNotFound(),
                            HttpStatus.NOT_FOUND
                    ));
            List<String> toEmails = this.userRepository.findEmailByRoleAndIsActive(role.getRoleId(), true);
            LocalDateTime date = LocalDateTime.now();

            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String subject = "Reporte automático de turnos asignados";
            String textPart = "Turnos de trabajo asignados para la fecha " + formattedDate + ".";
            String csvReportName = "Reporte " + formattedDate + ".csv";

            WorkShiftReportResponseDTO workShiftReport = getAssignedWorkShiftsByDate(date);

            byte[] csvReport = CsvGenerator.generateCsv(
                    workShiftReport.getAssignedWorkShiftResponseDTOList(),
                    headers,
                    nameMapping
            );

            this.mailSenderService.sendEmailWithCSVAttachment(
                    toEmails,
                    subject,
                    textPart,
                    csvReportName,
                    csvReport
            );

        }

    }

    @Override
    public void generateManualReport(String toEmail, LocalDateTime date) {

        List<String> toEmails = new ArrayList<>();
        toEmails.add(toEmail);

        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String subject = "Reporte manual de turnos asignados";
        String textPart = "Turnos de trabajo asignados para la fecha " + formattedDate + ".";
        String csvReportName = "Reporte " + formattedDate + ".csv";

        WorkShiftReportResponseDTO workShiftReport = getAssignedWorkShiftsByDate(date);

        byte[] csvReport = CsvGenerator.generateCsv(
                workShiftReport.getAssignedWorkShiftResponseDTOList(),
                headers,
                nameMapping
        );

        this.mailSenderService.sendEmailWithCSVAttachment(
                toEmails,
                subject,
                textPart,
                csvReportName,
                csvReport
        );

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