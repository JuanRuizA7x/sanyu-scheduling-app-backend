package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleExtensionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IScheduleExtensionRepository extends JpaRepository<ScheduleExtensionEntity, Long> {

    @Query(
            value = """
                    SELECT COUNT(S) FROM ScheduleExtensionEntity S
                    WHERE S.workShift.workShiftId = :workShiftId
                    """
    )
    Integer countByWorkShiftId(@Param("workShiftId") Long workShiftId);

}