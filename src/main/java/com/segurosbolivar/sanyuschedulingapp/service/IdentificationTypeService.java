package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.IdentificationTypeResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.mapper.IdentificationTypeEntityToIdentificationTypeResponseDTOMapper;
import com.segurosbolivar.sanyuschedulingapp.repository.IIdentificationTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdentificationTypeService implements IIdentificationTypeService {

    private final IIdentificationTypeRepository identificationTypeRepository;
    private final IdentificationTypeEntityToIdentificationTypeResponseDTOMapper identificationTypeEntityToIdentificationTypeResponseDTOMapper;

    public IdentificationTypeService(
            IIdentificationTypeRepository identificationTypeRepository,
            IdentificationTypeEntityToIdentificationTypeResponseDTOMapper identificationTypeEntityToIdentificationTypeResponseDTOMapper
    ) {
        this.identificationTypeRepository = identificationTypeRepository;
        this.identificationTypeEntityToIdentificationTypeResponseDTOMapper = identificationTypeEntityToIdentificationTypeResponseDTOMapper;
    }

    @Override
    public List<IdentificationTypeResponseDTO> findAll() {
        return this.identificationTypeEntityToIdentificationTypeResponseDTOMapper
                .map(this.identificationTypeRepository.findAll());
    }

    @Override
    public IdentificationTypeResponseDTO findByIdentificationTypeId(Long identificationTypeId) {
        return null;
    }

}