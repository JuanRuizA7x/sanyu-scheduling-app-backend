package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.request.ScheduleExtensionRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import com.segurosbolivar.sanyuschedulingapp.exception.ScheduleExtensionException;
import com.segurosbolivar.sanyuschedulingapp.exception.ScheduleExtensionExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftException;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.mapper.ScheduleExtensionRequestDTOToScheduleExtensionEntityMapper;
import com.segurosbolivar.sanyuschedulingapp.repository.IScheduleExtensionRepository;
import com.segurosbolivar.sanyuschedulingapp.repository.IWorkShiftRepository;
import com.segurosbolivar.sanyuschedulingapp.repository.ScheduleExtensionJDBCRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class ScheduleExtensionService implements IScheduleExtensionService{

    private final IScheduleExtensionRepository scheduleExtensionRepository;
    private final IWorkShiftRepository workShiftRepository;
    private final ScheduleExtensionJDBCRepository scheduleExtensionJDBCRepository;
    private final ScheduleExtensionRequestDTOToScheduleExtensionEntityMapper scheduleExtensionRequestDTOToScheduleExtensionEntityMapper;

    public ScheduleExtensionService(
            IScheduleExtensionRepository scheduleExtensionRepository,
            IWorkShiftRepository workShiftRepository,
            ScheduleExtensionJDBCRepository scheduleExtensionJDBCRepository,
            ScheduleExtensionRequestDTOToScheduleExtensionEntityMapper scheduleExtensionRequestDTOToScheduleExtensionEntityMapper
    ) {
        this.scheduleExtensionRepository = scheduleExtensionRepository;
        this.workShiftRepository = workShiftRepository;
        this.scheduleExtensionJDBCRepository = scheduleExtensionJDBCRepository;
        this.scheduleExtensionRequestDTOToScheduleExtensionEntityMapper = scheduleExtensionRequestDTOToScheduleExtensionEntityMapper;
    }

    /**
     * Extend the schedule for a work shift.
     *
     * @param scheduleExtensionRequestDTO The schedule extension request data.
     */
    @Override
    public void extendSchedule(ScheduleExtensionRequestDTO scheduleExtensionRequestDTO) {

        WorkShiftEntity workShift = getWorkShift(scheduleExtensionRequestDTO.getWorkShiftId());
        validateScheduleExtensionExistence(scheduleExtensionRequestDTO.getWorkShiftId());
        validateTimeFormat(scheduleExtensionRequestDTO.getStartTime());
        validateTimeFormat(scheduleExtensionRequestDTO.getEndTime());
        validateTimeRange(
                workShift, scheduleExtensionRequestDTO.getStartTime(), scheduleExtensionRequestDTO.getEndTime()
        );

        this.scheduleExtensionJDBCRepository.save(
                this.scheduleExtensionRequestDTOToScheduleExtensionEntityMapper.map(scheduleExtensionRequestDTO)
        );

    }

    /**
     * Get a work shift by its ID.
     *
     * @param workShiftId The ID of the work shift to retrieve.
     * @return The WorkShiftEntity representing the work shift.
     * @throws WorkShiftException if the work shift is not found.
     */
    private WorkShiftEntity getWorkShift(Long workShiftId) {
        return this.workShiftRepository.findById(workShiftId)
                .orElseThrow(() -> new WorkShiftException(
                        WorkShiftExceptionMessage.WORK_SHIFT_NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));
    }

    /**
     * Validate the existence of a schedule extension for a work shift.
     *
     * @param workShiftId The ID of the work shift to check for existing extensions.
     * @throws ScheduleExtensionException if a schedule extension already exists.
     */
    private void validateScheduleExtensionExistence(Long workShiftId) {

        Integer scheduleExtensionCount = this.scheduleExtensionRepository.countByWorkShiftId(workShiftId);

        if(scheduleExtensionCount != 0) {
            throw new ScheduleExtensionException(
                    ScheduleExtensionExceptionMessage.getScheduleExtensionAlreadyExists(),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    /**
     * Validate the time format for start and end times.
     *
     * @param time The time to validate.
     * @throws ScheduleExtensionException if the time has an invalid format.
     */
    private void validateTimeFormat(String time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        simpleDateFormat.setLenient(false);

        try {
            simpleDateFormat.parse(time);
        } catch(ParseException e) {
            throw new ScheduleExtensionException(
                    ScheduleExtensionExceptionMessage.getInvalidTimeFormat(),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    /**
     * Validate the time range for the schedule extension.
     *
     * @param workShift The work shift to extend.
     * @param startTime The start time of the extension.
     * @param endTime The end time of the extension.
     * @throws ScheduleExtensionException if the time range is invalid.
     */
    private void validateTimeRange(WorkShiftEntity workShift, String startTime, String endTime) {

        String[] splitStartTime = startTime.split(":");
        String[] splitEndTime = endTime.split(":");
        String[] splitScheduleStartTime = workShift.getSchedule().getStartTime().split(":");
        String[] splitScheduleEndTime = workShift.getSchedule().getEndTime().split(":");

        if(Integer.parseInt(splitEndTime[0]) < Integer.parseInt(splitStartTime[0])) {
            throw new ScheduleExtensionException(
                    ScheduleExtensionExceptionMessage.getInvalidEndTime(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(
                Integer.parseInt(splitEndTime[0]) == Integer.parseInt(splitStartTime[0]) &&
                Integer.parseInt(splitEndTime[1]) <= Integer.parseInt(splitStartTime[1])
        ) {
            throw new ScheduleExtensionException(
                    ScheduleExtensionExceptionMessage.getInvalidEndTime(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(
                Integer.parseInt(splitStartTime[0]) >= Integer.parseInt(splitScheduleStartTime[0]) &&
                Integer.parseInt(splitStartTime[0]) < Integer.parseInt(splitScheduleEndTime[0])
        ) {
            throw new ScheduleExtensionException(
                    ScheduleExtensionExceptionMessage.getInvalidTimeRange(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(
                Integer.parseInt(splitEndTime[0]) > Integer.parseInt(splitScheduleStartTime[0]) &&
                Integer.parseInt(splitEndTime[0]) <= Integer.parseInt(splitScheduleEndTime[0])
        ) {
            throw new ScheduleExtensionException(
                    ScheduleExtensionExceptionMessage.getInvalidTimeRange(),
                    HttpStatus.BAD_REQUEST
            );
        }

        if(
                Integer.parseInt(splitEndTime[0]) == Integer.parseInt(splitScheduleStartTime[0]) &&
                Integer.parseInt(splitEndTime[1]) > Integer.parseInt(splitScheduleStartTime[1])
        ) {
            throw new ScheduleExtensionException(
                    ScheduleExtensionExceptionMessage.getInvalidTimeRange(),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

}