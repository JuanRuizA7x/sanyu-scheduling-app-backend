package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.text.SimpleDateFormat;

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

}