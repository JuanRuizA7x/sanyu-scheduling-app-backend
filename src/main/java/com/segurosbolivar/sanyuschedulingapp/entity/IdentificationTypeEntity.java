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
@Table(name = "IDENTIFICATION_TYPE")
public class IdentificationTypeEntity {

    @Id
    @Column(name = "IDENTIFICATION_TYPE_ID")
    private Long identificationTypeId;
    @NotBlank
    @Size(max = 50)
    private String code;
    @NotBlank
    @Size(max = 50)
    private String name;
    @Size(max = 255)
    private String description;
    @NotNull
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @NotNull
    @Column(name = "LAST_MODIFICATION_DATE")
    private LocalDateTime lastModificationDate;

}