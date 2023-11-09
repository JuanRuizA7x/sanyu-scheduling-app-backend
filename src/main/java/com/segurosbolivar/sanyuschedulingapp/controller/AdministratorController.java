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

    /**
     * Retrieve a list of all schedules.
     *
     * @return A list of ScheduleResponseDTO objects.
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleResponseDTO>> findAllSchedules() {
        List<ScheduleResponseDTO> response = this.scheduleService.findAll();
        return new ResponseEntity<List<ScheduleResponseDTO>>(response, HttpStatus.OK);
    }

    /**
     * Find available contractors by role and date range.
     *
     * @param roleName   The role name for filtering contractors.
     * @param startDate  The start date for the date range.
     * @param endDate    The end date for the date range.
     * @return A list of UserResponseDTO objects.
     */
    @GetMapping("/available-contractors")
    public ResponseEntity<List<UserResponseDTO>> findAvailableContractorsByRoleDateRange(
            @RequestParam String roleName,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        List<UserResponseDTO> response = this.userService.findAvailableContractorsByRoleDateRange(roleName, startDate, endDate);
        return new ResponseEntity<List<UserResponseDTO>>(response, HttpStatus.OK);
    }

    /**
     * Assign multiple work shifts to a contractor.
     *
     * @param workShifts The request containing information about the work shifts to be assigned.
     * @return A map with the number of work shifts assigned.
     */
    @PostMapping("/assign-multiple-work-shifts")
    public ResponseEntity<Map<String, Integer>> assignMultipleWorkShifts(@Validated @RequestBody  MultipleWorkShiftsRequestDTO workShifts) {
        int affectedRows = this.workShiftService.assignMultipleWorkShifts(workShifts);
        Map<String, Integer> response = new HashMap<>();
        response.put("affectedRows", affectedRows);
        return new ResponseEntity<Map<String, Integer>>(response, HttpStatus.CREATED);
    }

    /**
     * Generates a manual report of the work shifts assigned on a specific date and sends it to a provided email.
     *
     * @param email The email address of the administrator.
     * @param date  The date for generating the report.
     * @return A ResponseEntity indicating success.
     */
    @GetMapping("/generate-report")
    public ResponseEntity<Void> getAssignedWorkShiftsByDate(@RequestParam String email, @RequestParam LocalDateTime date) {
        this.workShiftReportService.generateManualReport(email, date);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Process a CSV file to assign or update work shifts in bulk.
     *
     * @param csvFile The CSV file to be processed.
     * @return A CsvFileWorkShiftResponseDTO containing processing results.
     */
    @PostMapping("/upload-csv-file")
    public ResponseEntity<CsvFileWorkShiftResponseDTO> processCsvFile(@RequestBody MultipartFile csvFile) {
        validateFileExtension(csvFile);
        CsvFileWorkShiftResponseDTO csvFileWorkShiftResponseDTO = this.csvFileWorkShiftService.processCsvFile(csvFile);
        return new ResponseEntity<CsvFileWorkShiftResponseDTO>(csvFileWorkShiftResponseDTO, HttpStatus.OK);
    }

    /**
     * Validate the file extension for uploaded CSV files.
     *
     * @param csvFile The CSV file to be validated.
     * @throws CsvFileWorkShiftException if the file extension is invalid.
     */
    private void validateFileExtension(MultipartFile csvFile) {
        if (!Objects.equals(csvFile.getContentType(), "text/csv")) {
            throw new CsvFileWorkShiftException(
                    CsvFileWorkShiftExceptionMessage.getInvalidFileExtension(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * Find contractors by identification number pattern.
     *
     * @param identificationNumber The identification number pattern for filtering contractors.
     * @return A list of UserResponseDTO objects.
     */
    @GetMapping("/contractors/identification-number-like")
    public ResponseEntity<List<UserResponseDTO>> findByIdentificationNumberLikeAndIsActive(@RequestParam String identificationNumber) {
        List<UserResponseDTO> response = this.userService.findByIdentificationNumberLikeAndIsActive(identificationNumber);
        return new ResponseEntity<List<UserResponseDTO>>(response, HttpStatus.OK);
    }

    /**
     * Retrieve a list of work shifts for a contractor by user ID and date range.
     *
     * @param userId     The user ID for filtering work shifts.
     * @param startDate  The start date for the date range.
     * @param endDate    The end date for the date range.
     * @return A list of WorkShiftResponseDTO objects.
     */
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

    /**
     * Extend a schedule for a contractor's work shift.
     *
     * @param scheduleExtensionRequestDTO The request containing information about the schedule extension.
     * @return A ResponseEntity indicating success.
     */
    @PostMapping("/extend-schedule")
    public ResponseEntity<Void> extendSchedule(@RequestBody ScheduleExtensionRequestDTO scheduleExtensionRequestDTO) {
        this.scheduleExtensionService.extendSchedule(scheduleExtensionRequestDTO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}