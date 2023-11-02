package com.segurosbolivar.sanyuschedulingapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "USER")
public class UserEntity {

    @Id
    @Column(name = "USER_ID")
    private Long userId;
    @NotBlank
    @Size(max = 50)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Size(max = 50)
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @NotBlank
    @Size(max = 50)
    @Column(name = "LAST_NAME")
    private String lastName;
    @Size(max = 50)
    @Column(name = "SECOND_LAST_NAME", length = 50)
    private String secondLastName;
    @NotBlank
    @ManyToOne
    @JoinColumn(name = "IDENTIFICATION_TYPE_ID", nullable = false)
    private IdentificationTypeEntity identificationType;
    @NotNull
    @Size(max = 20)
    @Column(name = "IDENTIFICATION_NUMBER")
    private String identificationNumber;
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;
    @NotBlank
    @Size(max = 100)
    private String password;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private RoleEntity role;
    @NotNull
    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
    @NotNull
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @NotNull
    @Column(name = "LAST_MODIFICATION_DATE")
    private LocalDateTime lastModificationDate;

}