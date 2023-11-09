package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.UserResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.RoleEntity;
import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import com.segurosbolivar.sanyuschedulingapp.exception.*;
import com.segurosbolivar.sanyuschedulingapp.mapper.UserEntityToUserResponseDTOMapper;
import com.segurosbolivar.sanyuschedulingapp.repository.IRoleRepository;
import com.segurosbolivar.sanyuschedulingapp.repository.IUserRepository;
import com.segurosbolivar.sanyuschedulingapp.util.DateValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements IUserService {

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final UserEntityToUserResponseDTOMapper userEntityToUserResponseDTOMapper;

    public UserService(
            IRoleRepository roleRepository,
            IUserRepository userRepository,
            UserEntityToUserResponseDTOMapper userEntityToUserResponseDTOMapper
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userEntityToUserResponseDTOMapper = userEntityToUserResponseDTOMapper;
    }

    /**
     * Find a user by their user ID.
     *
     * @param userId The unique user ID.
     * @return UserResponseDTO containing user information.
     * @throws UserException if the user is not found.
     */
    @Override
    public UserResponseDTO findByUserId(Long userId) {

        UserEntity userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserException(
                        UserExceptionMessage.getUserNotFound(),
                        HttpStatus.NOT_FOUND
                ));

        return this.userEntityToUserResponseDTOMapper.map(userEntity);

    }

    /**
     * Find a user by their email address.
     *
     * @param email The email address of the user.
     * @return UserResponseDTO containing user information.
     * @throws UserException if the user is not found.
     */
    @Override
    public UserResponseDTO findByEmail(String email) {

        UserEntity userEntity = this.userRepository.findFirstByEmailAndIsActive(email, true)
                .orElseThrow(() -> new UserException(
                        UserExceptionMessage.getUserNotFound(),
                        HttpStatus.NOT_FOUND
                ));

        return this.userEntityToUserResponseDTOMapper.map(userEntity);
    }

    /**
     * Find available contractors by role within a specified date range.
     *
     * @param roleName   The role name of the contractors to search for.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @return List of UserResponseDTO objects containing contractor information.
     */
    @Override
    public List<UserResponseDTO> findAvailableContractorsByRoleDateRange(String roleName, LocalDateTime startDate, LocalDateTime endDate) {

        RoleEntity role = findRoleByName(roleName);
        DateValidator.validateDateRange(startDate, endDate);
        Date startTimeSQL = Date.valueOf(startDate.toLocalDate());
        Date endTimeSQL = Date.valueOf(endDate.toLocalDate());

        return this.userEntityToUserResponseDTOMapper
                .map(this.userRepository.findAvailableContractorsByRoleDateRange(role.getRoleId(), startTimeSQL, endTimeSQL));

    }

    /**
     * Find users by partial identification number.
     *
     * @param identificationNumber The partial identification number to search for.
     * @return List of UserResponseDTO objects matching the partial identification number.
     */
    @Override
    public List<UserResponseDTO> findByIdentificationNumberLikeAndIsActive(String identificationNumber) {
        return this.userEntityToUserResponseDTOMapper.map(
                this.userRepository.findByIdentificationNumberLikeAndIsActive(identificationNumber.trim(), true)
        );
    }

    /**
     * Find a role by its name.
     *
     * @param roleName The name of the role to search for.
     * @return RoleEntity containing role information.
     * @throws RoleException if the role is not found.
     */
    private RoleEntity findRoleByName(String roleName) {
        return this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleException(
                        RoleExceptionMessage.getRoleNotFound(),
                        HttpStatus.NOT_FOUND
                ));
    }

}