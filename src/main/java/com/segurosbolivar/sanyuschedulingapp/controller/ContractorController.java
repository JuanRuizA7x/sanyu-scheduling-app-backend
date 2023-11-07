package com.segurosbolivar.sanyuschedulingapp.controller;

import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.service.IWorkShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('Contratista Supervisor', 'Contratista de Campo')")
@RequestMapping("/api/contractor")
public class ContractorController {

    private final IWorkShiftService workShiftService;

    public ContractorController(IWorkShiftService workShiftService) {
        this.workShiftService = workShiftService;
    }

    @GetMapping("/work-shifts/email-and-date-range")
    public ResponseEntity<List<WorkShiftResponseDTO>> findByUserIdAndDateRange(
            @RequestParam String email,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        List<WorkShiftResponseDTO> response = this.workShiftService.findByEmailAndDateRange(
                email, startDate, endDate
        );
        return new ResponseEntity<List<WorkShiftResponseDTO>>(response, HttpStatus.OK);
    }

    @PutMapping("/notify-work-shift")
    public void markWorkShiftAsStarted(@RequestParam Long workShiftId) {
        this.workShiftService.markWorkShiftAsStarted(workShiftId);
    }

}