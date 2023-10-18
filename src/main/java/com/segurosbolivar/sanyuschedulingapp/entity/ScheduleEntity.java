package com.segurosbolivar.sanyuschedulingapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Column(name = "SCHEDULE_ID")
    private Long scheduleId;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(max = 5)
    @Column(name = "START_TIME")
    private String startTime;
    @NotBlank
    @Size(max = 5)
    @Column(name = "END_TIME")
    private String endTime;
    @Size(max = 5)
    @Column(name = "BREAK_START_TIME")
    private String breakStartTime;
    @Size(max = 5)
    @Column(name = "BREAK_END_TIME")
    private String breakEndTime;
    @NotNull
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @NotNull
    @Column(name = "LAST_MODIFICATION_DATE")
    private LocalDateTime lastModificationDate;

}