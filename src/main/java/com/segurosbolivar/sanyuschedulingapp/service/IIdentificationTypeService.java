package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.IdentificationTypeResponseDTO;

import java.util.List;

public interface IIdentificationTypeService {

    List<IdentificationTypeResponseDTO> findAll();
    IdentificationTypeResponseDTO findByIdentificationTypeId(Long identificationTypeId);

}