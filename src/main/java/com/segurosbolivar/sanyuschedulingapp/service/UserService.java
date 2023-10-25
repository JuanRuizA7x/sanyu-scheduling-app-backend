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

    @Override
    public UserResponseDTO findByUserId(Long userId) {

        UserEntity userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserException(
                        UserExceptionMessage.getUserNotFound(),
                        HttpStatus.NOT_FOUND
                ));

        return this.userEntityToUserResponseDTOMapper.map(userEntity);

    }

    @Override
    public UserResponseDTO findByEmail(String email) {

        UserEntity userEntity = this.userRepository.findFirstByEmailAndIsActive(email, true)
                .orElseThrow(() -> new UserException(
                        UserExceptionMessage.getUserNotFound(),
                        HttpStatus.NOT_FOUND
                ));

        return this.userEntityToUserResponseDTOMapper.map(userEntity);
    }

    @Override
    public List<UserResponseDTO> findAvailableContractorsByRoleDateRange(String roleName, LocalDateTime startDate, LocalDateTime endDate) {

        RoleEntity role = findRoleByName(roleName);
        DateValidator.validateDateRange(startDate, endDate);
        Date startTimeSQL = Date.valueOf(startDate.toLocalDate());
        Date endTimeSQL = Date.valueOf(endDate.toLocalDate());

        return this.userEntityToUserResponseDTOMapper
                .map(this.userRepository.findAvailableContractorsByRoleDateRange(role.getRoleId(), startTimeSQL, endTimeSQL));

    }

    public RoleEntity findRoleByName(String roleName) {
        return this.roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleException(
                        RoleExceptionMessage.getRoleNotFound(),
                        HttpStatus.NOT_FOUND
                ));
    }

}