package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.request.ScheduleExtensionRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleExtensionEntity;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleExtensionRequestDTOToScheduleExtensionEntityMapper implements IMapper<ScheduleExtensionRequestDTO, ScheduleExtensionEntity> {

    @Override
    public ScheduleExtensionEntity map(ScheduleExtensionRequestDTO scheduleExtension) {
        return ScheduleExtensionEntity.builder()
                .startTime(scheduleExtension.getStartTime())
                .endTime(scheduleExtension.getEndTime())
                .reason(scheduleExtension.getReason())
                .workShift(
                        WorkShiftEntity.builder()
                                .workShiftId(scheduleExtension.getWorkShiftId())
                                .build()
                )
                .build();
    }

    @Override
    public List<ScheduleExtensionEntity> map(List<ScheduleExtensionRequestDTO> scheduleExtensionList) {

        List<ScheduleExtensionEntity> response = new ArrayList<>();

        scheduleExtensionList.forEach(scheduleExtension -> {
            response.add(
                    ScheduleExtensionEntity.builder()
                            .startTime(scheduleExtension.getStartTime())
                            .endTime(scheduleExtension.getEndTime())
                            .reason(scheduleExtension.getReason())
                            .workShift(
                                    WorkShiftEntity.builder()
                                            .workShiftId(scheduleExtension.getWorkShiftId())
                                            .build()
                            )
                            .build()
            );
        });

        return response;

    }

}