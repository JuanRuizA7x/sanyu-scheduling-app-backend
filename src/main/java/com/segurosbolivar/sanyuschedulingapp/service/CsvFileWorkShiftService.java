package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.request.CsvFileWorkShiftRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.request.SingleWorkShiftRequestDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.CsvFileErrorResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.CsvFileWorkShiftResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.HolidayResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.RoleEntity;
import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleEntity;
import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import com.segurosbolivar.sanyuschedulingapp.enumeration.RoleEnum;
import com.segurosbolivar.sanyuschedulingapp.enumeration.ScheduleEnum;
import com.segurosbolivar.sanyuschedulingapp.exception.CsvFileWorkShiftExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.mapper.CsvRowToCsvFileWorkShiftRequestDTOMapper;
import com.segurosbolivar.sanyuschedulingapp.mapper.SingleWorkShiftRequestDTOToWorkShiftEntityMapper;
import com.segurosbolivar.sanyuschedulingapp.repository.*;
import com.segurosbolivar.sanyuschedulingapp.service.client.HolidayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CsvFileWorkShiftService implements ICsvFileWorkShiftService {

    private final WorkShiftJDBCRepository workShiftJDBCRepository;
    private final IRoleRepository roleRepository;
    private final IScheduleRepository scheduleRepository;
    private final IUserRepository userRepository;
    private final HolidayService holidayService;
    private final CsvRowToCsvFileWorkShiftRequestDTOMapper csvRowToCsvFileWorkShiftRequestDTOMapper;
    private final SingleWorkShiftRequestDTOToWorkShiftEntityMapper singleWorkShiftRequestDTOToWorkShiftEntityMapper;
    private CsvFileWorkShiftResponseDTO csvFileWorkShiftResponseDTO;

    public CsvFileWorkShiftService(
            WorkShiftJDBCRepository workShiftJDBCRepository,
            IRoleRepository roleRepository,
            IScheduleRepository scheduleRepository,
            IUserRepository userRepository,
            HolidayService holidayService,
            CsvRowToCsvFileWorkShiftRequestDTOMapper csvRowToCsvFileWorkShiftRequestDTOMapper,
            SingleWorkShiftRequestDTOToWorkShiftEntityMapper singleWorkShiftRequestDTOToWorkShiftEntityMapper
    ) {
        this.workShiftJDBCRepository = workShiftJDBCRepository;
        this.roleRepository = roleRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.holidayService = holidayService;
        this.csvRowToCsvFileWorkShiftRequestDTOMapper = csvRowToCsvFileWorkShiftRequestDTOMapper;
        this.singleWorkShiftRequestDTOToWorkShiftEntityMapper = singleWorkShiftRequestDTOToWorkShiftEntityMapper;
    }

    /**
     * Process a CSV file containing work shift data.
     *
     * This method processes a CSV file uploaded by the user, validates the data, and assigns work shifts accordingly.
     *
     * @param csvFile The CSV file containing work shift data.
     * @return A response containing information about the number of inserts, updates, and errors.
     */
    @Transactional
    @Override
    public CsvFileWorkShiftResponseDTO processCsvFile(MultipartFile csvFile) {

        this.csvFileWorkShiftResponseDTO = CsvFileWorkShiftResponseDTO.builder()
                .insertsCount(0)
                .updatesCount(0)
                .errors(new ArrayList<>())
                .build();

        List<CsvFileWorkShiftRequestDTO> csvFileWorkShiftRequestDTOList = readCsvFile(csvFile);

        for (int i = 1; i < csvFileWorkShiftRequestDTOList.size(); i++) {
            processCsvRow(csvFileWorkShiftRequestDTOList.get(i), (i + 1));
        }

        return this.csvFileWorkShiftResponseDTO;

    }

    /**
     * Process a single row of CSV data.
     *
     * This method processes a single row of CSV data, validates the content, and assigns work shifts.
     *
     * @param csvFileWorkShiftRequestDTO The CSV data to process.
     * @param row The row number in the CSV file.
     */
    private void processCsvRow(CsvFileWorkShiftRequestDTO csvFileWorkShiftRequestDTO, int row) {

        UserEntity user = getUserByIdentificationTypeAndIdentificationNumber(
                csvFileWorkShiftRequestDTO.getIdentificationType(),
                csvFileWorkShiftRequestDTO.getIdentificationNumber(),
                row
        );
        ScheduleEntity schedule = getScheduleByName(csvFileWorkShiftRequestDTO.getSchedule(), row);
        LocalDateTime startDate;
        LocalDateTime endDate;

        if(user == null || schedule == null) {
            return;
        }

        try {
            startDate = LocalDateTime.parse(csvFileWorkShiftRequestDTO.getStartDate() + "T00:00:00");
            endDate = LocalDateTime.parse(csvFileWorkShiftRequestDTO.getEndDate() + "T00:00:00");
        } catch (DateTimeParseException e) {

            this.csvFileWorkShiftResponseDTO.addError(
                    CsvFileErrorResponseDTO.builder()
                            .row(row)
                            .message(CsvFileWorkShiftExceptionMessage.getInvalidDateFormat())
                            .build()
            );

            return;

        }

        if(
                !validateDateRange(startDate, endDate, row) ||
                !validateRoleAndScheduleCompatibility(user, schedule, row)
        ) {
            return;
        }

        String countryCode = "CO";
        List<String> holidays = getHolidays(startDate.getYear(), endDate.getYear(), countryCode);
        LocalDateTime currentWorkShiftDate = startDate;
        long totalDays = Duration.between(startDate, endDate).toDays() + 1;

        for(int i = 0; i < totalDays; i++) {
            SingleWorkShiftRequestDTO workShift = SingleWorkShiftRequestDTO.builder()
                    .date(currentWorkShiftDate)
                    .scheduleId(schedule.getScheduleId())
                    .userId(user.getUserId())
                    .build();
            assignWorkShift(workShift, holidays);
            currentWorkShiftDate = currentWorkShiftDate.plusDays(1);
        }

    }

    /**
     * Read data from a CSV file.
     *
     * This method reads data from the provided CSV file and maps it to DTOs for processing.
     *
     * @param csvFile The CSV file to read.
     * @return A list of DTOs representing the data from the CSV file.
     */
    private List<CsvFileWorkShiftRequestDTO> readCsvFile(MultipartFile csvFile) {

        List<CsvFileWorkShiftRequestDTO> csvFileWorkShiftRequestDTOList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {

            String csvRow;

            while ((csvRow = reader.readLine()) != null) {
                String[] csvRowDetails = csvRow.split(",");
                csvFileWorkShiftRequestDTOList.add(
                        this.csvRowToCsvFileWorkShiftRequestDTOMapper.map(csvRowDetails)
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return csvFileWorkShiftRequestDTOList;

    }

    /**
     * Assign a work shift based on the provided work shift request.
     *
     * This method assigns a work shift based on the provided `SingleWorkShiftRequestDTO` and checks for holidays.
     *
     * @param workShiftRequest The work shift request to process and assign.
     * @param holidays List of holidays to consider when assigning work shifts.
     */
    private void assignWorkShift(SingleWorkShiftRequestDTO workShiftRequest, List<String> holidays) {

        WorkShiftEntity existingWorkShift = this.workShiftJDBCRepository.findByUserIdAndDate(
                workShiftRequest.getUserId(),
                Date.valueOf(workShiftRequest.getDate().toLocalDate())
        );
        String workShiftDate = workShiftRequest.getDate().toLocalDate().toString();

        if(
                (!holidays.contains(workShiftDate)) &&
                        (workShiftRequest.getDate().getDayOfWeek() != DayOfWeek.SATURDAY) &&
                        (workShiftRequest.getDate().getDayOfWeek() != DayOfWeek.SUNDAY)
        ) {

            if(existingWorkShift == null) {

                WorkShiftEntity newWorkShift = this.singleWorkShiftRequestDTOToWorkShiftEntityMapper
                        .map(workShiftRequest);
                newWorkShift.setIsStarted(false);

                this.csvFileWorkShiftResponseDTO.incrementInsertsCount(
                        this.workShiftJDBCRepository.save(newWorkShift)
                );

            } else {

                existingWorkShift.setDate(workShiftRequest.getDate());
                existingWorkShift.setSchedule(
                        ScheduleEntity.builder().scheduleId(workShiftRequest.getScheduleId()).build()
                );
                existingWorkShift.setUserId(workShiftRequest.getUserId());
                existingWorkShift.setIsStarted(false);

                this.csvFileWorkShiftResponseDTO.incrementUpdatesCount(
                        this.workShiftJDBCRepository.update(existingWorkShift)
                );

            }

        }

    }

    /**
     * Retrieve a list of holidays for a given date range and country code.
     *
     * This method retrieves a list of holidays within a specified date range and for a given country code.
     *
     * @param startYear The start year for the date range.
     * @param endYear The end year for the date range.
     * @param countryCode The country code for which to retrieve holidays.
     * @return A list of holiday dates.
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
     * Retrieve a user by identification type and identification number.
     *
     * This method retrieves a user by their identification type and number and validates the user's existence.
     *
     * @param identificationType The identification type of the user.
     * @param identificationNumber The identification number of the user.
     * @param row The row number in the CSV file (for error reporting).
     * @return The UserEntity object representing the user if found, or null if not found.
     */
    private UserEntity getUserByIdentificationTypeAndIdentificationNumber(
            String identificationType, String identificationNumber, int row
    ) {

        UserEntity user = this.userRepository.findByIdentificationTypeAndIdentificationNumberAndIsActive(
                identificationType,
                identificationNumber,
                true
        ).orElse(null);

        validateUser(user, identificationType, identificationNumber, row);

        return user;

    }

    /**
     * Retrieve a schedule by name.
     *
     * This method retrieves a schedule by its name and validates the schedule's existence.
     *
     * @param name The name of the schedule to retrieve.
     * @param row The row number in the CSV file (for error reporting).
     * @return The ScheduleEntity object representing the schedule if found, or null if not found.
     */
    private ScheduleEntity getScheduleByName(String name, int row) {

        ScheduleEntity schedule = this.scheduleRepository.findByName(
                name.toLowerCase()
        ).orElse(null);

        validateSchedule(schedule, name, row);

        return schedule;

    }

    /**
     * Validate the existence of a user.
     *
     * This method checks if a user exists and, if not, adds an error to the response.
     *
     * @param user The UserEntity object to validate.
     * @param identificationType The identification type used for error reporting.
     * @param identificationNumber The identification number used for error reporting.
     * @param row The row number in the CSV file (for error reporting).
     */
    private void validateUser(UserEntity user, String identificationType, String identificationNumber, int row) {
        if(user == null) {

            this.csvFileWorkShiftResponseDTO.addError(
                    CsvFileErrorResponseDTO.builder()
                            .row(row)
                            .message(CsvFileWorkShiftExceptionMessage.getUserNotFound(
                                    identificationType + " " + identificationNumber
                            ))
                            .build()
            );

        }
    }

    /**
     * Validate the existence of a schedule.
     *
     * This method checks if a schedule exists and, if not, adds an error to the response.
     *
     * @param schedule The ScheduleEntity object to validate.
     * @param name The name of the schedule used for error reporting.
     * @param row The row number in the CSV file (for error reporting).
     */
    private void validateSchedule(ScheduleEntity schedule, String name, int row) {
        if(schedule == null) {

            this.csvFileWorkShiftResponseDTO.addError(
                    CsvFileErrorResponseDTO.builder()
                            .row(row)
                            .message(CsvFileWorkShiftExceptionMessage.getScheduleNotFound(name))
                            .build()
            );

        }
    }

    /**
     * Validate the date range for work shifts.
     *
     * This method checks if the date range for work shifts is valid and adds errors to the response if not.
     *
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @param row The row number in the CSV file (for error reporting).
     * @return True if the date range is valid, false otherwise.
     */
    private boolean validateDateRange(LocalDateTime startDate, LocalDateTime endDate, int row) {

        Date startDateSQL = Date.valueOf(startDate.toLocalDate());
        Date endDateSQL = Date.valueOf(endDate.toLocalDate());
        Date currentDate = Date.valueOf(LocalDate.now());

        int startDateValidation = startDateSQL.compareTo(currentDate);
        int endDateValidation = endDateSQL.compareTo(startDateSQL);

        if(startDateValidation < 1) {
            this.csvFileWorkShiftResponseDTO.addError(
                    CsvFileErrorResponseDTO.builder()
                            .row(row)
                            .message(CsvFileWorkShiftExceptionMessage.getInvalidStartDate())
                            .build()
            );
            return false;
        }

        if(endDateValidation < 0) {
            this.csvFileWorkShiftResponseDTO.addError(
                    CsvFileErrorResponseDTO.builder()
                            .row(row)
                            .message(CsvFileWorkShiftExceptionMessage.getInvalidEndDate())
                            .build()
            );
            return false;
        }

        return true;

    }

    /**
     * Validate the compatibility of role and schedule.
     *
     * This method checks if the role and schedule are compatible and adds errors to the response if not.
     *
     * @param user The UserEntity object representing the user's role.
     * @param schedule The ScheduleEntity object representing the schedule.
     * @param row The row number in the CSV file (for error reporting).
     * @return True if the role and schedule are compatible, false otherwise.
     */
    private boolean validateRoleAndScheduleCompatibility(UserEntity user, ScheduleEntity schedule, int row) {

        RoleEntity role = this.roleRepository.findById(user.getRole().getRoleId()).orElse(null);

        assert role != null;
        if(
                !Objects.equals(role.getName(), RoleEnum.SUPERVISOR_CONTRACTOR.getDescription()) &&
                        !Objects.equals(role.getName(), RoleEnum.FIELD_CONTRACTOR.getDescription())
        ) {

            this.csvFileWorkShiftResponseDTO.addError(
                    CsvFileErrorResponseDTO.builder()
                            .row(row)
                            .message(CsvFileWorkShiftExceptionMessage.getInvalidRoleAssignment())
                            .build()
            );

            return false;

        } else if(
                Objects.equals(role.getName(), RoleEnum.SUPERVISOR_CONTRACTOR.getDescription()) &&
                        !Objects.equals(schedule.getName(), ScheduleEnum.OFFICE.getDescription())
        ) {

            this.csvFileWorkShiftResponseDTO.addError(
                    CsvFileErrorResponseDTO.builder()
                            .row(row)
                            .message(CsvFileWorkShiftExceptionMessage.getInvalidScheduleAssignment(schedule.getName(), role.getName()))
                            .build()
            );

            return false;

        } else if(
                !Objects.equals(role.getName(), RoleEnum.SUPERVISOR_CONTRACTOR.getDescription()) &&
                        Objects.equals(schedule.getName(), ScheduleEnum.OFFICE.getDescription())
        ) {

            this.csvFileWorkShiftResponseDTO.addError(
                    CsvFileErrorResponseDTO.builder()
                            .row(row)
                            .message(CsvFileWorkShiftExceptionMessage.getInvalidScheduleAssignment(schedule.getName(), role.getName()))
                            .build()
            );

            return false;

        }

        return true;

    }

}