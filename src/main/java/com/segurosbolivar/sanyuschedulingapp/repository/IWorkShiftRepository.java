package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkShiftRepository extends JpaRepository<WorkShiftEntity, Long> {
}