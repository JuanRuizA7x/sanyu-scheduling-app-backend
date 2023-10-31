package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    @Query(
            value = """
                    SELECT s FROM ScheduleEntity s
                    WHERE lower(s.name) = :name
                    """
    )
    Optional<ScheduleEntity> findByName(String name);

}