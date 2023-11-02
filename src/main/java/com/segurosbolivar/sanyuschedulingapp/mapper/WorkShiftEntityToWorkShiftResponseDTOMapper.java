package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkShiftEntityToWorkShiftResponseDTOMapper implements IMapper<WorkShiftEntity, WorkShiftResponseDTO> {

    private final ScheduleEntityToScheduleResponseDTOMapper scheduleEntityToScheduleResponseDTOMapper;
    private final ScheduleExtensionEntityToScheduleExtensionResponseDTOMapper scheduleExtensionEntityToScheduleExtensionResponseDTOMapper;

    public WorkShiftEntityToWorkShiftResponseDTOMapper(
            ScheduleEntityToScheduleResponseDTOMapper scheduleEntityToScheduleResponseDTOMapper,
            ScheduleExtensionEntityToScheduleExtensionResponseDTOMapper scheduleExtensionEntityToScheduleExtensionResponseDTOMapper
    ) {
        this.scheduleEntityToScheduleResponseDTOMapper = scheduleEntityToScheduleResponseDTOMapper;
        this.scheduleExtensionEntityToScheduleExtensionResponseDTOMapper = scheduleExtensionEntityToScheduleExtensionResponseDTOMapper;
    }

    @Override
    public WorkShiftResponseDTO map(WorkShiftEntity workShift) {
        return WorkShiftResponseDTO.builder()
                .workShiftId(workShift.getWorkShiftId())
                .date(workShift.getDate())
                .schedule(this.scheduleEntityToScheduleResponseDTOMapper.map(workShift.getSchedule()))
                .userId(workShift.getUserId())
                .scheduleExtensions(
                        this.scheduleExtensionEntityToScheduleExtensionResponseDTOMapper.map(
                                workShift.getScheduleExtensions()
                        )
                )
                .isStarted(workShift.getIsStarted())
                .startedAt(workShift.getStartedAt())
                .build();
    }

    @Override
    public List<WorkShiftResponseDTO> map(List<WorkShiftEntity> workShiftList) {

        List<WorkShiftResponseDTO> response = new ArrayList<>();

        workShiftList.forEach(workShift -> {
            response.add(
                    WorkShiftResponseDTO.builder()
                            .workShiftId(workShift.getWorkShiftId())
                            .date(workShift.getDate())
                            .schedule(this.scheduleEntityToScheduleResponseDTOMapper.map(workShift.getSchedule()))
                            .userId(workShift.getUserId())
                            .scheduleExtensions(
                                    this.scheduleExtensionEntityToScheduleExtensionResponseDTOMapper.map(
                                            workShift.getScheduleExtensions()
                                    )
                            )
                            .isStarted(workShift.getIsStarted())
                            .startedAt(workShift.getStartedAt())
                            .build()
            );
        });

        return response;

    }

}