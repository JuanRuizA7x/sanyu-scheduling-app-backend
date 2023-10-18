package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.IdentificationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIdentificationTypeRepository extends JpaRepository<IdentificationTypeEntity, Long> {
}