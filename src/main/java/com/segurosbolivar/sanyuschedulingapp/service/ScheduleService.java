package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.ScheduleResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleEntity;
import com.segurosbolivar.sanyuschedulingapp.exception.ScheduleException;
import com.segurosbolivar.sanyuschedulingapp.exception.ScheduleExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.mapper.ScheduleEntityToScheduleResponseDTOMapper;
import com.segurosbolivar.sanyuschedulingapp.repository.IScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService implements IScheduleService {

    private final IScheduleRepository scheduleRepository;
    private final ScheduleEntityToScheduleResponseDTOMapper scheduleEntityToScheduleResponseDTOMapper;

    public ScheduleService(
            IScheduleRepository scheduleRepository,
            ScheduleEntityToScheduleResponseDTOMapper scheduleEntityToScheduleResponseDTOMapper
    ) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleEntityToScheduleResponseDTOMapper = scheduleEntityToScheduleResponseDTOMapper;
    }

    /**
     * Retrieve a list of all schedules.
     *
     * @return A list of ScheduleResponseDTO representing all schedules.
     */
    @Override
    public List<ScheduleResponseDTO> findAll() {
        return this.scheduleEntityToScheduleResponseDTOMapper
                .map(this.scheduleRepository.findAll());
    }

    /**
     * Find a schedule by its ID.
     *
     * @param scheduleId The ID of the schedule to retrieve.
     * @return The ScheduleResponseDTO representing the schedule.
     * @throws ScheduleException if the schedule is not found.
     */
    @Override
    public ScheduleResponseDTO findByScheduleId(Long scheduleId) {

        ScheduleEntity schedule = this.scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(
                        ScheduleExceptionMessage.getScheduleNotFound(),
                        HttpStatus.NOT_FOUND
                ));

        return this.scheduleEntityToScheduleResponseDTOMapper
                .map(schedule);

    }

}