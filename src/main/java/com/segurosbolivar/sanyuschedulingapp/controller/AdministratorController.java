package com.segurosbolivar.sanyuschedulingapp.controller;

import com.segurosbolivar.sanyuschedulingapp.dto.request.MultipleWorkShiftsRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.request.ScheduleExtensionRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.CsvFileWorkShiftResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.ScheduleResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.UserResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.exception.CsvFileWorkShiftException;
import com.segurosbolivar.sanyuschedulingapp.exception.CsvFileWorkShiftExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@PreAuthorize("hasRole('Administrador')")
@RequestMapping("/api/administrator")
public class AdministratorController {

    private final ICsvFileWorkShiftService csvFileWorkShiftService;
    private final IScheduleService scheduleService;
    private final IScheduleExtensionService scheduleExtensionService;
    private final IUserService userService;
    private final IWorkShiftService workShiftService;
    private final IWorkShiftReportService workShiftReportService;

    public AdministratorController(
            ICsvFileWorkShiftService csvFileWorkShiftService, IScheduleService scheduleService,
            IScheduleExtensionService scheduleExtensionService,
            IUserService userService,
            IWorkShiftService workShiftService,
            IWorkShiftReportService workShiftReportService
    ) {
        this.csvFileWorkShiftService = csvFileWorkShiftService;
        this.scheduleService = scheduleService;
        this.scheduleExtensionService = scheduleExtensionService;
        this.userService = userService;
        this.workShiftService = workShiftService;
        this.workShiftReportService = workShiftReportService;
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponseDTO>> findAllSchedules() {
        List<ScheduleResponseDTO> response = this.scheduleService.findAll();
        return new ResponseEntity<List<ScheduleResponseDTO>>(response, HttpStatus.OK);
    }

    @GetMapping("/available-contractors")
    public ResponseEntity<List<UserResponseDTO>> findAvailableContractorsByRoleDateRange(
            @RequestParam String roleName,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        List<UserResponseDTO> response = this.userService.findAvailableContractorsByRoleDateRange(roleName, startDate, endDate);
        return new ResponseEntity<List<UserResponseDTO>>(response, HttpStatus.OK);
    }

    @PostMapping("/assign-multiple-work-shifts")
    public ResponseEntity<Map<String, Integer>> assignMultipleWorkShifts(@Validated @RequestBody  MultipleWorkShiftsRequestDTO workShifts) {
        int affectedRows = this.workShiftService.assignMultipleWorkShifts(workShifts);
        Map<String, Integer> response = new HashMap<>();
        response.put("affectedRows", affectedRows);
        return new ResponseEntity<Map<String, Integer>>(response, HttpStatus.CREATED);
    }

    @GetMapping("/generate-report")
    public ResponseEntity<Void> getAssignedWorkShiftsByDate(@RequestParam String email, @RequestParam LocalDateTime date) {
        this.workShiftReportService.generateManualReport(email, date);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/upload-csv-file")
    public ResponseEntity<CsvFileWorkShiftResponseDTO> processCsvFile(@RequestBody MultipartFile csvFile) {
        validateFileExtension(csvFile);
        CsvFileWorkShiftResponseDTO csvFileWorkShiftResponseDTO = this.csvFileWorkShiftService.processCsvFile(csvFile);
        return new ResponseEntity<CsvFileWorkShiftResponseDTO>(csvFileWorkShiftResponseDTO, HttpStatus.OK);
    }

    private void validateFileExtension(MultipartFile csvFile) {
        if (!Objects.equals(csvFile.getContentType(), "text/csv")) {
            throw new CsvFileWorkShiftException(
                    CsvFileWorkShiftExceptionMessage.getInvalidFileExtension(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/contractors/identification-number-like")
    public ResponseEntity<List<UserResponseDTO>> findByIdentificationNumberLikeAndIsActive(@RequestParam String identificationNumber) {
        List<UserResponseDTO> response = this.userService.findByIdentificationNumberLikeAndIsActive(identificationNumber);
        return new ResponseEntity<List<UserResponseDTO>>(response, HttpStatus.OK);
    }

    @GetMapping("/work-shifts/user-id-and-date-range")
    public ResponseEntity<List<WorkShiftResponseDTO>> findByUserIdAndDateRange(
            @RequestParam Long userId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        List<WorkShiftResponseDTO> response = this.workShiftService.findByUserIdAndDateRange(
                userId, startDate, endDate
        );
        return new ResponseEntity<List<WorkShiftResponseDTO>>(response, HttpStatus.OK);
    }

    @PostMapping("/extend-schedule")
    public ResponseEntity<Void> extendSchedule(@RequestBody ScheduleExtensionRequestDTO scheduleExtensionRequestDTO) {
        this.scheduleExtensionService.extendSchedule(scheduleExtensionRequestDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}