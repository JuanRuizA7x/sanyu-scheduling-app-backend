package com.segurosbolivar.sanyuschedulingapp.controller;

import com.segurosbolivar.sanyuschedulingapp.dto.IdentificationTypeResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.service.IIdentificationTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/identification-types")
public class IdentificationTypeController {

    private final IIdentificationTypeService identificationTypeService;

    public IdentificationTypeController(IIdentificationTypeService identificationTypeService) {
        this.identificationTypeService = identificationTypeService;
    }

    @GetMapping
    @PreAuthorize("hasRole('Administrador')")
    public ResponseEntity<List<IdentificationTypeResponseDTO>> getHelloWorld() {
        List<IdentificationTypeResponseDTO> response = this.identificationTypeService.findAll();
        return new ResponseEntity<List<IdentificationTypeResponseDTO>>(response, HttpStatus.OK);
    }

}