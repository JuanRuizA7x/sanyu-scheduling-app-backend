package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findFirstByEmailAndIsActive(String email, Boolean isActive);

}