package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.request.SingleWorkShiftRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleEntity;
import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SingleWorkShiftRequestDTOToWorkShiftEntityMapper implements IMapper<SingleWorkShiftRequestDTO, WorkShiftEntity> {

    @Override
    public WorkShiftEntity map(SingleWorkShiftRequestDTO workShift) {
        return WorkShiftEntity.builder()
                .date(workShift.getDate())
                .schedule(ScheduleEntity.builder().scheduleId(workShift.getScheduleId()).build())
                .user(UserEntity.builder().userId(workShift.getUserId()).build())
                .build();
    }

    @Override
    public List<WorkShiftEntity> map(List<SingleWorkShiftRequestDTO> workShiftList) {

        List<WorkShiftEntity> response = new ArrayList<>();

        workShiftList.forEach(workShift -> {
            response.add(
                    WorkShiftEntity.builder()
                            .date(workShift.getDate())
                            .schedule(ScheduleEntity.builder().scheduleId(workShift.getScheduleId()).build())
                            .user(UserEntity.builder().userId(workShift.getUserId()).build())
                            .build()
            );
        });

        return response;

    }

}