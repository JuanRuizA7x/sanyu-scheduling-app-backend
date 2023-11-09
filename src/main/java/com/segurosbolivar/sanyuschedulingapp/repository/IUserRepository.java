package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * The queries made below are written with a native query
 * because the 'user' table conflicts with an Oracle reserved word,
 * so it must be specified in this way: "USER".
 */
@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    @NotNull
    @Query(
            value = """
                    SELECT * FROM "USER"
                    WHERE USER_ID = :userId
                    """,
            nativeQuery = true
    )
    Optional<UserEntity> findById(@NotNull @Param("userId") Long userId);

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
    List<UserEntity> findAvailableContractorsByRoleDateRange(
            @Param("roleId") Long roleId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query(
            value = """
                    SELECT EMAIL FROM "USER"
                    WHERE ROLE_ID = :roleId
                    AND IS_ACTIVE = :isActive
                    """,
            nativeQuery = true
    )
    List<String> findEmailByRoleAndIsActive(@Param("roleId") Long roleId, @Param("isActive") Boolean isActive);

    @Query(
            value = """
                    SELECT U.* FROM "USER" U
                    JOIN IDENTIFICATION_TYPE I
                    ON (U.IDENTIFICATION_TYPE_ID = I.IDENTIFICATION_TYPE_ID)
                    WHERE I.CODE = :identificationType
                    AND U.IDENTIFICATION_NUMBER = :identificationNumber
                    AND U.IS_ACTIVE = :isActive
                    ORDER BY USER_ID ASC
                    FETCH FIRST 1 ROW ONLY
                    """,
            nativeQuery = true
    )
    Optional<UserEntity> findByIdentificationTypeAndIdentificationNumberAndIsActive(
            @Param("identificationType") String identificationType,
            @Param("identificationNumber") String identificationNumber,
            @Param("isActive") Boolean isActive
    );

    @Query(
            value = """
                    SELECT * FROM "USER"
                    WHERE IDENTIFICATION_NUMBER LIKE (:identificationNumber || '%')
                    AND IS_ACTIVE = :isActive
                    AND ROLE_ID NOT IN (
                        SELECT ROLE_ID
                        FROM ROLE
                        WHERE NAME = 'Administrador'
                    )
                    """,
            nativeQuery = true
    )
    List<UserEntity> findByIdentificationNumberLikeAndIsActive(
            @Param("identificationNumber") String identificationNumber,
            @Param("isActive") Boolean isActive
    );

}