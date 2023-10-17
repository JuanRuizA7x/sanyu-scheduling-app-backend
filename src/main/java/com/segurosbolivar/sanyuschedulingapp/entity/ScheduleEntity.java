package com.segurosbolivar.sanyuschedulingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "SCHEDULE")
public class ScheduleEntity {

    @Id
    @Column(name = "schedule_id")
    private Long scheduleId;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(name = "start_time", length = 5, nullable = false)
    private String startTime;
    @Column(name = "end_time", length = 5, nullable = false)
    private String endTime;
    @Column(name = "break_start_time", length = 5)
    private String breakStartTime;
    @Column(name = "break_end_time", length = 5)
    private String breakEndTime;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastModificationDate;

}