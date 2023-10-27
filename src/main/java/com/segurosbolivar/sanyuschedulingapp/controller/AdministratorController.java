package com.segurosbolivar.sanyuschedulingapp.controller;

import com.segurosbolivar.sanyuschedulingapp.dto.request.MultipleWorkShiftsRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.ScheduleResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.UserResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftReportResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('Administrador')")
@RequestMapping("/api/administrator")
public class AdministratorController {

    private final IScheduleService scheduleService;
    private final IScheduleExtensionService scheduleExtensionService;
    private final IUserService userService;
    private final IWorkShiftService workShiftService;
    private final IWorkShiftReportService workShiftReportService;

    public AdministratorController(
            IScheduleService scheduleService,
            IScheduleExtensionService scheduleExtensionService,
            IUserService userService,
            IWorkShiftService workShiftService,
            IWorkShiftReportService workShiftReportService
    ) {
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
    public ResponseEntity<WorkShiftReportResponseDTO> getAssignedWorkShiftsByDate(@RequestParam LocalDateTime date) {
        WorkShiftReportResponseDTO response = this.workShiftReportService.getAssignedWorkShiftsByDate(date);
        return new ResponseEntity<WorkShiftReportResponseDTO>(response, HttpStatus.OK);
    }

}