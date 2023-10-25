package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.IdentificationTypeResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.IdentificationTypeEntity;
import com.segurosbolivar.sanyuschedulingapp.exception.IdentificationTypeException;
import com.segurosbolivar.sanyuschedulingapp.exception.IdentificationTypeExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.mapper.IdentificationTypeEntityToIdentificationTypeResponseDTOMapper;
import com.segurosbolivar.sanyuschedulingapp.repository.IIdentificationTypeRepository;
import org.springframework.http.HttpStatus;
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

        IdentificationTypeEntity identificationType = this.identificationTypeRepository.findById(identificationTypeId)
                .orElseThrow(() -> new IdentificationTypeException(
                        IdentificationTypeExceptionMessage.getIdentificationTypeNotFound(),
                        HttpStatus.NOT_FOUND
                ));

        return this.identificationTypeEntityToIdentificationTypeResponseDTOMapper
                .map(identificationType);

    }

}