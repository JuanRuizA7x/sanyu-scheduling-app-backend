package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.response.IdentificationTypeResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.IdentificationTypeEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IdentificationTypeEntityToIdentificationTypeResponseDTOMapper implements IMapper<IdentificationTypeEntity, IdentificationTypeResponseDTO> {

    @Override
    public IdentificationTypeResponseDTO map(IdentificationTypeEntity identificationType) {
        return IdentificationTypeResponseDTO.builder()
                .identificationTypeId(identificationType.getIdentificationTypeId())
                .code(identificationType.getCode())
                .name(identificationType.getName())
                .description(identificationType.getDescription())
                .build();
    }

    @Override
    public List<IdentificationTypeResponseDTO> map(List<IdentificationTypeEntity> identificationTypeList) {

        List<IdentificationTypeResponseDTO> response = new ArrayList<>();

        identificationTypeList.forEach(identificationType -> {
            response.add(
                    IdentificationTypeResponseDTO.builder()
                            .identificationTypeId(identificationType.getIdentificationTypeId())
                            .code(identificationType.getCode())
                            .name(identificationType.getName())
                            .description(identificationType.getDescription())
                            .build()
            );
        });

        return response;

    }

}