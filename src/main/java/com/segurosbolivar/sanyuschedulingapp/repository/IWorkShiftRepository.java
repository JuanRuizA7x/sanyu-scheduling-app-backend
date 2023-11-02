package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.WorkShiftEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IWorkShiftRepository extends JpaRepository<WorkShiftEntity, Long> {

    @Query(
            value = """
                    SELECT * FROM WORK_SHIFT
                    WHERE USER_ID = :userId
                    AND "DATE" BETWEEN :startDate AND :endDate
                    ORDER BY "DATE"
                    """,
            nativeQuery = true
    )
    List<WorkShiftEntity> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @NotNull
    @Query(
            value = """
                    SELECT * FROM WORK_SHIFT
                    WHERE WORK_SHIFT_ID = :workShiftId
                    """,
            nativeQuery = true
    )
    Optional<WorkShiftEntity> findById(@NotNull @Param("workShiftId") Long workShiftId);

}