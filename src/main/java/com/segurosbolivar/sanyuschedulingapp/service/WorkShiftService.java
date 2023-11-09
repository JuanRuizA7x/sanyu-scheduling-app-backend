package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.request.MultipleWorkShiftsRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.request.SingleWorkShiftRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.*;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import com.segurosbolivar.sanyuschedulingapp.enumeration.RoleEnum;
import com.segurosbolivar.sanyuschedulingapp.enumeration.ScheduleEnum;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftException;
import com.segurosbolivar.sanyuschedulingapp.exception.WorkShiftExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.mapper.SingleWorkShiftRequestDTOToWorkShiftEntityMapper;
import com.segurosbolivar.sanyuschedulingapp.mapper.WorkShiftEntityToWorkShiftResponseDTOMapper;
import com.segurosbolivar.sanyuschedulingapp.repository.IWorkShiftRepository;
import com.segurosbolivar.sanyuschedulingapp.repository.WorkShiftJDBCRepository;
import com.segurosbolivar.sanyuschedulingapp.service.client.HolidayService;
import com.segurosbolivar.sanyuschedulingapp.util.DateValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WorkShiftService implements IWorkShiftService {

    private final IWorkShiftRepository workShiftRepository;
    private final IScheduleService scheduleService;
    private final IUserService userService;
    private final HolidayService holidayService;
    private final SingleWorkShiftRequestDTOToWorkShiftEntityMapper singleWorkShiftRequestDTOToWorkShiftEntityMapper;
    private final WorkShiftEntityToWorkShiftResponseDTOMapper workShiftEntityToWorkShiftResponseDTOMapper;
    private final WorkShiftJDBCRepository workShiftJDBCRepository;

    public WorkShiftService(
            IWorkShiftRepository workShiftRepository,
            IScheduleService scheduleService,
            IUserService userService,
            HolidayService holidayService,
            SingleWorkShiftRequestDTOToWorkShiftEntityMapper singleWorkShiftRequestDTOToWorkShiftEntityMapper,
            WorkShiftEntityToWorkShiftResponseDTOMapper workShiftEntityToWorkShiftResponseDTOMapper,
            WorkShiftJDBCRepository workShiftJDBCRepository
    ) {
        this.workShiftRepository = workShiftRepository;
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.holidayService = holidayService;
        this.singleWorkShiftRequestDTOToWorkShiftEntityMapper = singleWorkShiftRequestDTOToWorkShiftEntityMapper;
        this.workShiftEntityToWorkShiftResponseDTOMapper = workShiftEntityToWorkShiftResponseDTOMapper;
        this.workShiftJDBCRepository = workShiftJDBCRepository;
    }

    /**
     * Service for assigning multiple work shifts to a user within a date range.
     *
     * This method allows for the bulk assignment of work shifts for a specific user over a specified date range.
     *
     * @param workShifts The request containing information about multiple work shifts to be assigned.
     * @return The number of work shifts successfully assigned.
     */
    @Transactional
    @Override
    public int assignMultipleWorkShifts(MultipleWorkShiftsRequestDTO workShifts) {

        int affectedRows = 0;

        DateValidator.validateDateRange(workShifts.getStartDate(), workShifts.getEndDate());
        validateDateRangeAvailability(
                workShifts.getUserId(),
                Date.valueOf(workShifts.getStartDate().toLocalDate()),
                Date.valueOf(workShifts.getEndDate().toLocalDate())
        );
        ScheduleResponseDTO schedule = this.scheduleService.findByScheduleId(workShifts.getScheduleId());
        UserResponseDTO user = this.userService.findByUserId(workShifts.getUserId());
        validateRoleAndScheduleCompatibility(user.getRole().getName(), schedule.getName());

        int startYear = workShifts.getStartDate().getYear();
        int endYear = workShifts.getEndDate().getYear();
        String countryCode = "CO";
        List<String> holidays = getHolidays(startYear, endYear, countryCode);

        SingleWorkShiftRequestDTO currentWorkShift = SingleWorkShiftRequestDTO.builder()
                .date(workShifts.getStartDate())
                .scheduleId(workShifts.getScheduleId())
                .userId(workShifts.getUserId())
                .build();
        LocalDateTime currentWorkShiftDate = workShifts.getStartDate();
        long totalDays = Duration.between(workShifts.getStartDate(), workShifts.getEndDate()).toDays() + 1;

        for(int i = 0; i < totalDays; i++) {
            currentWorkShift.setDate(currentWorkShiftDate);
            affectedRows += assignSingleWorkShift(currentWorkShift, holidays);
            currentWorkShiftDate = currentWorkShiftDate.plusDays(1);
        }

        return affectedRows;

    }

    /**
     * Find work shifts for a user within a specified date range.
     *
     * @param userId The ID of the user for whom work shifts are to be retrieved.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return A list of WorkShiftResponseDTO representing the work shifts within the date range.
     */
    @Override
    public List<WorkShiftResponseDTO> findByUserIdAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {

        Date formattedStartDate = Date.valueOf(startDate.toLocalDate());
        Date formattedEndDate = Date.valueOf(endDate.toLocalDate());

        return this.workShiftEntityToWorkShiftResponseDTOMapper.map(
                this.workShiftRepository.findByUserIdAndDateRange(userId, formattedStartDate, formattedEndDate)
        );

    }

    /**
     * Find work shifts for a user by email within a specified date range.
     *
     * @param email The email address of the user for whom work shifts are to be retrieved.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return A list of WorkShiftResponseDTO representing the work shifts within the date range.
     */
    @Override
    public List<WorkShiftResponseDTO> findByEmailAndDateRange(String email, LocalDateTime startDate, LocalDateTime endDate) {

        UserResponseDTO user = this.userService.findByEmail(email);

        return findByUserIdAndDateRange(user.getUserId(), startDate, endDate);

    }

    /**
     * Mark a work shift as started.
     *
     * @param workShiftId The ID of the work shift to mark as started.
     */
    @Override
    public void markWorkShiftAsStarted(Long workShiftId) {

        this.workShiftRepository.findById(workShiftId)
                .orElseThrow(() -> new WorkShiftException(
                        WorkShiftExceptionMessage.getWorkShiftNotFound(),
                        HttpStatus.NOT_FOUND
                ));

        this.workShiftJDBCRepository.markWorkShiftAsStarted(workShiftId);

    }

    /**
     * Assign a single work shift to a user on a specified date, given the list of holidays.
     *
     * @param workShift The request containing information about a single work shift.
     * @param holidays List of holidays.
     * @return The number of work shifts successfully assigned (0 if the date is a holiday or weekend).
     */
    private int assignSingleWorkShift(SingleWorkShiftRequestDTO workShift, List<String> holidays) {

        String workShiftDate = workShift.getDate().toLocalDate().toString();

        if(
                (!holidays.contains(workShiftDate)) &&
                (workShift.getDate().getDayOfWeek() != DayOfWeek.SATURDAY) &&
                (workShift.getDate().getDayOfWeek() != DayOfWeek.SUNDAY)
        ) {
            WorkShiftEntity workShiftEntity = this.singleWorkShiftRequestDTOToWorkShiftEntityMapper
                    .map(workShift);
            workShiftEntity.setIsStarted(false);
            return this.workShiftJDBCRepository.save(workShiftEntity);
        }

        return 0;

    }

    /**
     * Get a list of holidays for a given date range and country code.
     *
     * @param startYear The start year of the date range.
     * @param endYear The end year of the date range.
     * @param countryCode The country code for which to retrieve holidays.
     * @return A list of holiday dates as strings.
     */
    private List<String> getHolidays(int startYear, int endYear, String countryCode) {

        List<HolidayResponseDTO> holidays = new ArrayList<>();
        List<String> response = new ArrayList<>();

        for (int i = startYear; i <= endYear; i++) {
            holidays.addAll(this.holidayService.getHolidaysByYearAndCountryCode(i, countryCode));
        }

        holidays.forEach(holiday -> response.add(holiday.getDate()));

        return response;

    }

    /**
     * Validate the availability of work shifts within a specified date range for a user.
     *
     * @param userId The ID of the user.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @throws WorkShiftException if the date range is not available for assignment.
     */
    private void validateDateRangeAvailability(Long userId, Date startDate, Date endDate) {

        Integer assignedWorkShiftsCount = this.workShiftJDBCRepository.countByUserIdAndDateRange(userId, startDate, endDate);

        if (assignedWorkShiftsCount == null || assignedWorkShiftsCount != 0) {
            throw new WorkShiftException(
                    WorkShiftExceptionMessage.getInvalidDateRange(),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    /**
     * Validate the compatibility of a user's role and schedule for work shift assignment.
     *
     * @param roleName The name of the user's role.
     * @param scheduleName The name of the schedule.
     * @throws WorkShiftException if the role and schedule are incompatible for assignment.
     */
    private void validateRoleAndScheduleCompatibility(String roleName, String scheduleName) {

        if(
                !Objects.equals(roleName, RoleEnum.SUPERVISOR_CONTRACTOR.getDescription()) &&
                !Objects.equals(roleName, RoleEnum.FIELD_CONTRACTOR.getDescription())
        ) {
            throw new WorkShiftException(
                    WorkShiftExceptionMessage.getInvalidRoleAssignment(),
                    HttpStatus.BAD_REQUEST
            );
        } else if(
                Objects.equals(roleName, RoleEnum.SUPERVISOR_CONTRACTOR.getDescription()) &&
                !Objects.equals(scheduleName, ScheduleEnum.OFFICE.getDescription())
        ) {
            throw new WorkShiftException(
                    WorkShiftExceptionMessage.getInvalidScheduleAssignment(scheduleName, roleName),
                    HttpStatus.BAD_REQUEST
            );
        } else if(
                !Objects.equals(roleName, RoleEnum.SUPERVISOR_CONTRACTOR.getDescription()) &&
                Objects.equals(scheduleName, ScheduleEnum.OFFICE.getDescription())
        ) {
            throw new WorkShiftException(
                    WorkShiftExceptionMessage.getInvalidScheduleAssignment(scheduleName, roleName),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

}