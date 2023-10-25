package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/*
* Las consultas que se hacen a continuación están escritas con query nativo
* ya que la tabla user entra en conflicto con una palabra reservada de Oracle,
* por lo cual se debe especificar de esta forma: "USER"
* */
@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    @Query(
            value = """
                    SELECT * FROM "USER"
                    WHERE USER_ID = :userId
                    """,
            nativeQuery = true
    )
    @Override
    Optional<UserEntity> findById(@Param("userId") Long userId);

    @Query(
            value = """
                    SELECT * FROM "USER"
                    WHERE EMAIL = :email
                    AND IS_ACTIVE = :isActive
                    ORDER BY USER_ID ASC
                    FETCH FIRST 1 ROW ONLY
                    """,
            nativeQuery = true
    )
    Optional<UserEntity> findFirstByEmailAndIsActive(@Param("email") String email, @Param("isActive") Boolean isActive);

    @Query(
            value = """
                    SELECT * FROM "USER"
                    WHERE ROLE_ID = :roleId
                    AND USER_ID NOT IN (
                        SELECT USER_ID
                        FROM WORK_SHIFT
                        WHERE "DATE" BETWEEN :startDate AND :endDate
                    )
                    """,
            nativeQuery = true
    )
    List<UserEntity> findAvailableContractorsByDateRange(
            @Param("roleId") Long roleId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

}