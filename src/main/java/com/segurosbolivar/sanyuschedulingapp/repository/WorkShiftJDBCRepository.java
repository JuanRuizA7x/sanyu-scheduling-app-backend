package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.dto.response.AssignedWorkShiftResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.dto.response.WorkShiftReportResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository
public class WorkShiftJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public WorkShiftJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(WorkShiftEntity workShift) {

        String sqlCommand = """
                    INSERT INTO WORK_SHIFT ("DATE", SCHEDULE_ID, USER_ID, IS_STARTED)
                    VALUES (?, ?, ?, ?)
                """;

        return this.jdbcTemplate.update(
                sqlCommand,
                workShift.getDate(),
                workShift.getSchedule().getScheduleId(),
                workShift.getUser().getUserId(),
                false
        );

    }

    public Integer countByUserIdAndDateRange(Long userId, Date startDate, Date endDate) {

        String sqlCommand = """
            SELECT COUNT(*) FROM WORK_SHIFT
            WHERE USER_ID = ?
            AND "DATE" BETWEEN ? AND ?
        """;

        return jdbcTemplate.queryForObject(sqlCommand, Integer.class, userId, startDate, endDate);

    }

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

    private List<AssignedWorkShiftResponseDTO> mapResultSetToDTO(Object[] arrayData) throws SQLException {

        List<AssignedWorkShiftResponseDTO> result = new ArrayList<>();

        for (Object element : arrayData) {
            Struct struct = (Struct) element;
            Object[] attributes = struct.getAttributes();

            AssignedWorkShiftResponseDTO dto = AssignedWorkShiftResponseDTO.builder()
                    .fullName((String) attributes[0])
                    .identificationType((String) attributes[1])
                    .identificationNumber((String) attributes[2])
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