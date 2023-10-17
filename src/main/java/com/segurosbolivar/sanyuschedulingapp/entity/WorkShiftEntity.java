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
@Table(name = "WORK_SHIFT")
public class WorkShiftEntity {

    @Id
    @Column(name = "work_shift_id")
    private Long workShiftId;
    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @Column(name = "is_started", nullable = false)
    private Boolean isStarted;
    @Column(name = "startedAt")
    private LocalDateTime startedAt;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastModificationDate;

}