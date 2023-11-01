package com.segurosbolivar.sanyuschedulingapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WORK_SHIFT")
public class WorkShiftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_SHIFT_ID")
    private Long workShiftId;
    @NotNull
    @Column(name = "DATE")
    private LocalDateTime date;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
    private ScheduleEntity schedule;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;
    @NotNull
    @Column(name = "IS_STARTED")
    private Boolean isStarted;
    @OneToMany(mappedBy = "workShift", cascade = CascadeType.ALL)
    private List<ScheduleExtensionEntity> scheduleExtensions;
    @Column(name = "STARTED_AT")
    private LocalDateTime startedAt;
    @NotNull
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @NotNull
    @Column(name = "LAST_MODIFICATION_DATE")
    private LocalDateTime lastModificationDate;

}