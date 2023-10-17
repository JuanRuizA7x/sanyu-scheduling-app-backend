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
@Table(name = "SCHEDULE_EXTENSION")
public class ScheduleExtensionEntity {

    @Id
    @Column(name = "schedule_extension_id")
    private Long scheduleExtensionId;
    @Column(name = "start_time", length = 5, nullable = false)
    private String startTime;
    @Column(name = "end_time", length = 5, nullable = false)
    private String endTime;
    @Column(length = 500, nullable = false)
    private String reason;
    @ManyToOne
    @JoinColumn(name = "work_shift_id", nullable = false)
    private WorkShiftEntity workShift;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastModificationDate;

}