package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.dto.response.AssignedWorkShiftResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftReportResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleEntity;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class WorkShiftJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkShiftJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a WorkShiftEntity to the database.
     *
     * @param workShift The WorkShiftEntity to be saved to the database.
     * @return The number of rows affected by the insert operation.
     */
    public int save(WorkShiftEntity workShift) {

        String sqlCommand = """
                    INSERT INTO WORK_SHIFT ("DATE", SCHEDULE_ID, USER_ID, IS_STARTED)
                    VALUES (?, ?, ?, ?)
                """;

        return this.jdbcTemplate.update(
                sqlCommand,
                workShift.getDate(),
                workShift.getSchedule().getScheduleId(),
                workShift.getUserId(),
                workShift.getIsStarted()
        );

    }

    /**
     * Update a WorkShiftEntity in the database.
     *
     * @param workShift The WorkShiftEntity to be updated in the database.
     * @return The number of rows affected by the update operation.
     */
    public int update(WorkShiftEntity workShift) {

        String sqlCommand = """
                    UPDATE WORK_SHIFT
                    SET
                        "DATE" = ?,
                        SCHEDULE_ID = ?,
                        USER_ID = ?,
                        IS_STARTED = ?,
                        LAST_MODIFICATION_DATE = CURRENT_TIMESTAMP
                    WHERE WORK_SHIFT_ID = ?
                """;

        return this.jdbcTemplate.update(
                sqlCommand,
                workShift.getDate(),
                workShift.getSchedule().getScheduleId(),
                workShift.getUserId(),
                workShift.getIsStarted(),
                workShift.getWorkShiftId()
        );

    }

    /**
     * Queries the WORK_SHIFT table in the database to retrieve a WorkShiftEntity with the specified user ID and date. If no record is found, it returns null.
     *
     * @param userId The user ID for which to retrieve the WorkShiftEntity.
     * @param date The date for which to retrieve the WorkShiftEntity.
     * @return The retrieved WorkShiftEntity or null if no record is found.
     */
    public WorkShiftEntity findByUserIdAndDate(Long userId, Date date) {

        String sqlCommand = """
                    SELECT * FROM WORK_SHIFT
                    WHERE USER_ID = ?
                    AND "DATE" = ?
                    FETCH FIRST 1 ROW ONLY
                """;

        RowMapper<WorkShiftEntity> rowMapper = (rs, rowNum) -> {
            return WorkShiftEntity.builder()
                    .workShiftId(rs.getLong(1))
                    .date(rs.getObject(2, LocalDateTime.class))
                    .schedule(ScheduleEntity.builder().scheduleId(rs.getLong(3)).build())
                    .userId(rs.getLong(4))
                    .isStarted(rs.getBoolean(5))
                    .creationDate(rs.getObject(6, LocalDateTime.class))
                    .lastModificationDate(rs.getObject(7, LocalDateTime.class))
                    .build();
        };

        List<WorkShiftEntity> workShifts = jdbcTemplate.query(
                sqlCommand,
                rowMapper,
                new Object[]{userId, date}
        );

        return workShifts.isEmpty() ? null : workShifts.get(0);

    }

    /**
     * Count the number of WorkShiftEntities by user ID and date range.
     *
     * @param userId The user ID for which to count WorkShiftEntities.
     * @param startDate The start date of the date range.
     * @param endDate The end date of the date range.
     * @return The count of WorkShiftEntities.
     */
    public Integer countByUserIdAndDateRange(Long userId, Date startDate, Date endDate) {

        String sqlCommand = """
                    SELECT COUNT(*) FROM WORK_SHIFT
                    WHERE USER_ID = ?
                    AND "DATE" BETWEEN ? AND ?
                """;

        return jdbcTemplate.queryForObject(sqlCommand, Integer.class, userId, startDate, endDate);

    }

    /**
     * Updates the specified WorkShiftEntity in the database, setting the "isStarted" flag to 1 (true) and updating the "started_at" and "lastModificationDate" fields.
     *
     * @param workShiftId The ID of the WorkShiftEntity to mark as started.
     */
    public void markWorkShiftAsStarted(Long workShiftId) {

        String sqlCommand = """
                    UPDATE WORK_SHIFT
                    SET
                        IS_STARTED = 1,
                        STARTED_AT = CURRENT_TIMESTAMP,
                        LAST_MODIFICATION_DATE = CURRENT_TIMESTAMP
                    WHERE WORK_SHIFT_ID = ?
                """;

        this.jdbcTemplate.update(
                sqlCommand,
                workShiftId
        );

    }

    /**
     * Calls a stored procedure to retrieve assigned work shifts for the specified date. It maps the results to a list of AssignedWorkShiftResponseDTO objects.
     *
     * @param date The date for which to retrieve assigned work shifts.
     * @return A WorkShiftReportResponseDTO containing a list of assigned work shifts and an error message, if applicable.
     * @throws SQLException If there is an SQL-related error during the database operation.
     */
    public WorkShiftReportResponseDTO getAssignedWorkShiftsByDate(Date date) throws SQLException {

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("APP_AGEND_UT.GET_ASSIGNED_WORK_SHIFTS_BY_DATE")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_DATE", Types.DATE),
                        new SqlOutParameter("P_REPORT", Types.ARRAY, "WORK_SHIFT_REPORT_LIST"),
                        new SqlOutParameter("P_ERROR_MESSAGE", Types.VARCHAR)
                );

        SqlParameterSource inputParams = new MapSqlParameterSource()
                .addValue("P_DATE", date);

        Map<String, Object> result = simpleJdbcCall.execute(inputParams);
        String errorMessage = (String) result.get("P_ERROR_MESSAGE");
        List<AssignedWorkShiftResponseDTO> assignedWorkShiftResponseDTOList = new ArrayList<>();

        Array array = (Array) result.get("P_REPORT");
        Object[] arrayData = (Object[]) array.getArray();
        assignedWorkShiftResponseDTOList = mapResultSetToDTO(arrayData);

        return WorkShiftReportResponseDTO.builder()
                .assignedWorkShiftResponseDTOList(assignedWorkShiftResponseDTOList)
                .errorMessage(errorMessage)
                .build();

    }

    /**
     * Maps the result set from the stored procedure to a list of AssignedWorkShiftResponseDTO objects.
     *
     * @param arrayData The result set data to be mapped.
     * @return A list of AssignedWorkShiftResponseDTO objects.
     * @throws SQLException If there is an SQL-related error during the mapping process.
     */
    private List<AssignedWorkShiftResponseDTO> mapResultSetToDTO(Object[] arrayData) throws SQLException {

        List<AssignedWorkShiftResponseDTO> result = new ArrayList<>();

        for (Object element : arrayData) {
            Struct struct = (Struct) element;
            Object[] attributes = struct.getAttributes();

            AssignedWorkShiftResponseDTO dto = AssignedWorkShiftResponseDTO.builder()
                    .identificationType((String) attributes[0])
                    .identificationNumber((String) attributes[1])
                    .fullName((String) attributes[2])
                    .email((String) attributes[3])
                    .role((String) attributes[4])
                    .schedule((String) attributes[5])
                    .startTime((String) attributes[6])
                    .endTime((String) attributes[7])
                    .isStarted((String) attributes[8])
                    .extensionStartTime((String) attributes[9])
                    .extensionEndTime((String) attributes[10])
                    .extensionReason((String) attributes[11])
                    .build();

            result.add(dto);
        }

        return result;

    }

}