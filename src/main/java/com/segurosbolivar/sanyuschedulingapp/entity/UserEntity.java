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
@Table(name = "USER")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;
    @Column(name = "middle_name", length = 50)
    private String middleName;
    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;
    @Column(name = "second_last_name", length = 50)
    private String secondLastName;
    @ManyToOne
    @JoinColumn(name = "identification_type_id", nullable = false)
    private IdentificationTypeEntity identificationType;
    @Column(name = "identification_number", length = 50, nullable = false)
    private String identificationNumber;
    @Column(length = 50, nullable = false)
    private String email;
    @Column(length = 50, nullable = false)
    private String password;
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    @Column(name = "last_modification_date", nullable = false)
    private LocalDateTime lastModificationDate;

}