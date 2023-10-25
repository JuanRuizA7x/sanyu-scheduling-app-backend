package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.response.ScheduleResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleEntityToScheduleResponseDTOMapper implements IMapper<ScheduleEntity, ScheduleResponseDTO> {

    @Override
    public ScheduleResponseDTO map(ScheduleEntity schedule) {
        return ScheduleResponseDTO.builder()
                .scheduleId(schedule.getScheduleId())
                .name(schedule.getName())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .breakStartTime(schedule.getBreakStartTime())
                .breakEndTime(schedule.getBreakEndTime())
                .build();
    }

    @Override
    public List<ScheduleResponseDTO> map(List<ScheduleEntity> scheduleList) {

        List<ScheduleResponseDTO> response = new ArrayList<>();

       scheduleList.forEach(schedule -> {
            response.add(
                    ScheduleResponseDTO.builder()
                            .scheduleId(schedule.getScheduleId())
                            .name(schedule.getName())
                            .startTime(schedule.getStartTime())
                            .endTime(schedule.getEndTime())
                            .breakStartTime(schedule.getBreakStartTime())
                            .breakEndTime(schedule.getBreakEndTime())
                            .build()
            );
        });

        return response;

    }

}