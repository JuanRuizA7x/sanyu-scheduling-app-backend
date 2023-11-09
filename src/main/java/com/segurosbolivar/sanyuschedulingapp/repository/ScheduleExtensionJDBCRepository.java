package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleExtensionEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleExtensionJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleExtensionJDBCRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a ScheduleExtensionEntity to the database.
     *
     * @param scheduleExtension The ScheduleExtensionEntity to be saved to the database.
     */
    public void save(ScheduleExtensionEntity scheduleExtension) {

        String sqlCommand = """
                    INSERT INTO SCHEDULE_EXTENSION (START_TIME, END_TIME, REASON, WORK_SHIFT_ID)
                    VALUES (?, ?, ?, ?)
                """;

        this.jdbcTemplate.update(
                sqlCommand,
                scheduleExtension.getStartTime(),
                scheduleExtension.getEndTime(),
                scheduleExtension.getReason(),
                scheduleExtension.getWorkShift().getWorkShiftId()
        );

    }

}