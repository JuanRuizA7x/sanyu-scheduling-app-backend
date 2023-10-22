package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.UserResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import com.segurosbolivar.sanyuschedulingapp.exception.UserException;
import com.segurosbolivar.sanyuschedulingapp.exception.UserExceptionMessage;
import com.segurosbolivar.sanyuschedulingapp.mapper.UserEntityToUserResponseDTOMapper;
import com.segurosbolivar.sanyuschedulingapp.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    private final UserEntityToUserResponseDTOMapper userEntityToUserResponseDTOMapper;

    public UserService(
            IUserRepository userRepository,
            UserEntityToUserResponseDTOMapper userEntityToUserResponseDTOMapper
    ) {
        this.userRepository = userRepository;
        this.userEntityToUserResponseDTOMapper = userEntityToUserResponseDTOMapper;
    }

    @Override
    public UserResponseDTO findUserByEmail(String email) {

        UserEntity userEntity = this.userRepository.findFirstByEmailAndIsActive(email, true)
                .orElseThrow(() -> new UserException(
                        UserExceptionMessage.getUserNotFound(),
                        HttpStatus.NOT_FOUND
                ));

        return this.userEntityToUserResponseDTOMapper.map(userEntity);

    }

}