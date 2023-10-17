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
@Table(name = "IDENTIFICATION_TYPE")
public class IdentificationTypeEntity {

    @Id
    @Column(name = "identification_type_id")
    private Long identificationTypeId;
    @Column(length = 50, nullable = false)
    private String code;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 255)
    private String description;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastModificationDate;

}