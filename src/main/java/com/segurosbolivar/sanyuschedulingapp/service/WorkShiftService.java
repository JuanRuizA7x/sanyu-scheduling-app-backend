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
import com.segurosbolivar.sanyuschedulingapp.mapper.WorkShiftEntityToWorkShiftResponseDTO;
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
    private final WorkShiftEntityToWorkShiftResponseDTO workShiftEntityToWorkShiftResponseDTO;
    private final WorkShiftJDBCRepository workShiftJDBCRepository;

    public WorkShiftService(
            IWorkShiftRepository workShiftRepository,
            IScheduleService scheduleService,
            IUserService userService,
            HolidayService holidayService,
            SingleWorkShiftRequestDTOToWorkShiftEntityMapper singleWorkShiftRequestDTOToWorkShiftEntityMapper,
            WorkShiftEntityToWorkShiftResponseDTO workShiftEntityToWorkShiftResponseDTO,
            WorkShiftJDBCRepository workShiftJDBCRepository
    ) {
        this.workShiftRepository = workShiftRepository;
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.holidayService = holidayService;
        this.singleWorkShiftRequestDTOToWorkShiftEntityMapper = singleWorkShiftRequestDTOToWorkShiftEntityMapper;
        this.workShiftEntityToWorkShiftResponseDTO = workShiftEntityToWorkShiftResponseDTO;
        this.workShiftJDBCRepository = workShiftJDBCRepository;
    }

    @Override
    public List<WorkShiftResponseDTO> findAllByUserIdAndStartDate(Long userId, LocalDateTime startDate) {
        return null;
    }

    @Override
    public WorkShiftResponseDTO findByWorkShiftId(Long workShiftId) {
        return null;
    }

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

    @Override
    public int assignSingleWorkShift(SingleWorkShiftRequestDTO workShift, List<String> holidays) {

        String workShiftDate = workShift.getDate().toLocalDate().toString();

        if(
                (!holidays.contains(workShiftDate)) &&
                (workShift.getDate().getDayOfWeek() != DayOfWeek.SATURDAY) &&
                (workShift.getDate().getDayOfWeek() != DayOfWeek.SUNDAY)
        ) {
            WorkShiftEntity workShiftEntity = this.singleWorkShiftRequestDTOToWorkShiftEntityMapper
                    .map(workShift);
            workShiftEntity.setIsStarted(true);
            return this.workShiftJDBCRepository.save(workShiftEntity);
        }

        return 0;

    }

    public List<String> getHolidays(int startYear, int endYear, String countryCode) {

        List<HolidayResponseDTO> holidays = new ArrayList<>();
        List<String> response = new ArrayList<>();

        for (int i = startYear; i <= endYear; i++) {
            holidays.addAll(this.holidayService.getHolidaysByYearAndCountryCode(i, countryCode));
        }

        holidays.forEach(holiday -> response.add(holiday.getDate()));

        return response;

    }

    public void validateDateRangeAvailability(Long userId, Date startDate, Date endDate) {

        Integer assignedWorkShiftsCount = this.workShiftJDBCRepository.countByUserIdAndDateRange(userId, startDate, endDate);

        if (assignedWorkShiftsCount == null || assignedWorkShiftsCount != 0) {
            throw new WorkShiftException(
                    WorkShiftExceptionMessage.getInvalidDateRange(),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    public void validateRoleAndScheduleCompatibility(String roleName, String scheduleName) {

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