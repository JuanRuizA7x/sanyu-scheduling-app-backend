package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleExtensionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IScheduleExtensionRepository extends JpaRepository<ScheduleExtensionEntity, Long> {
}