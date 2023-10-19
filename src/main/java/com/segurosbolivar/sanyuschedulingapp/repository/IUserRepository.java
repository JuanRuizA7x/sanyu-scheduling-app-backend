package com.segurosbolivar.sanyuschedulingapp.repository;

import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

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

}