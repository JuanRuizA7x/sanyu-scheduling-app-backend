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
@Table(name = "SCHEDULE_EXTENSION")
public class ScheduleExtensionEntity {

    @Id
    @Column(name = "SCHEDULE_EXTENSION_ID")
    private Long scheduleExtensionId;
    @NotBlank
    @Size(min = 5, max = 5)
    @Column(name = "START_TIME")
    private String startTime;
    @NotBlank
    @Size(min = 5, max = 5)
    @Column(name = "END_TIME")
    private String endTime;
    @NotBlank
    @Size(max = 500)
    private String reason;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "WORK_SHIFT_ID")
    private WorkShiftEntity workShift;
    @NotNull
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @NotNull
    @Column(name = "LAST_MODIFICATION_DATE")
    private LocalDateTime lastModificationDate;

}