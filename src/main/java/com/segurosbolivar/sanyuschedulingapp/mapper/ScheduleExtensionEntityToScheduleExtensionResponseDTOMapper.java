package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.response.ScheduleExtensionResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleExtensionEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleExtensionEntityToScheduleExtensionResponseDTOMapper implements IMapper<ScheduleExtensionEntity, ScheduleExtensionResponseDTO> {

    @Override
    public ScheduleExtensionResponseDTO map(ScheduleExtensionEntity scheduleExtension) {
        return ScheduleExtensionResponseDTO.builder()
                .scheduleExtensionId(scheduleExtension.getScheduleExtensionId())
                .startTime(scheduleExtension.getStartTime())
                .endTime(scheduleExtension.getEndTime())
                .reason(scheduleExtension.getReason())
                .build();
    }

    @Override
    public List<ScheduleExtensionResponseDTO> map(List<ScheduleExtensionEntity> scheduleExtensionList) {

        List<ScheduleExtensionResponseDTO> response = new ArrayList<>();

        scheduleExtensionList.forEach(scheduleExtension -> {
            response.add(
                    ScheduleExtensionResponseDTO.builder()
                            .scheduleExtensionId(scheduleExtension.getScheduleExtensionId())
                            .startTime(scheduleExtension.getStartTime())
                            .endTime(scheduleExtension.getEndTime())
                            .reason(scheduleExtension.getReason())
                            .build()
            );
        });

        return response;

    }

}