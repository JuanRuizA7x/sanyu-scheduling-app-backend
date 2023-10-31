package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkShiftEntityToWorkShiftResponseDTOMapper implements IMapper<WorkShiftEntity, WorkShiftResponseDTO> {

    private final ScheduleEntityToScheduleResponseDTOMapper scheduleEntityToScheduleResponseDTOMapper;
    private final UserEntityToUserResponseDTOMapper userEntityToUserResponseDTOMapper;

    public WorkShiftEntityToWorkShiftResponseDTOMapper(
            ScheduleEntityToScheduleResponseDTOMapper scheduleEntityToScheduleResponseDTOMapper,
            UserEntityToUserResponseDTOMapper userEntityToUserResponseDTOMapper
    ) {
        this.scheduleEntityToScheduleResponseDTOMapper = scheduleEntityToScheduleResponseDTOMapper;
        this.userEntityToUserResponseDTOMapper = userEntityToUserResponseDTOMapper;
    }

    @Override
    public WorkShiftResponseDTO map(WorkShiftEntity workShift) {
        return WorkShiftResponseDTO.builder()
                .workShiftId(workShift.getWorkShiftId())
                .date(workShift.getDate())
                .schedule(this.scheduleEntityToScheduleResponseDTOMapper.map(workShift.getSchedule()))
                .user(this.userEntityToUserResponseDTOMapper.map(workShift.getUser()))
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
                            .user(this.userEntityToUserResponseDTOMapper.map(workShift.getUser()))
                            .isStarted(workShift.getIsStarted())
                            .startedAt(workShift.getStartedAt())
                            .build()
            );
        });

        return response;

    }

}