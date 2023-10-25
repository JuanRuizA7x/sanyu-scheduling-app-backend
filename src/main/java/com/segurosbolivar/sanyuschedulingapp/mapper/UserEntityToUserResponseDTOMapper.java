package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.response.UserResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserEntityToUserResponseDTOMapper implements IMapper<UserEntity, UserResponseDTO> {

    private final IdentificationTypeEntityToIdentificationTypeResponseDTOMapper identificationTypeEntityToIdentificationTypeResponseDTOMapper;
    private final RoleEntityToRoleResponseDTOMapper roleEntityToRoleResponseDTOMapper;

    public UserEntityToUserResponseDTOMapper(
            IdentificationTypeEntityToIdentificationTypeResponseDTOMapper identificationTypeEntityToIdentificationTypeResponseDTOMapper,
            RoleEntityToRoleResponseDTOMapper roleEntityToRoleResponseDTOMapper
    ) {
        this.identificationTypeEntityToIdentificationTypeResponseDTOMapper = identificationTypeEntityToIdentificationTypeResponseDTOMapper;
        this.roleEntityToRoleResponseDTOMapper = roleEntityToRoleResponseDTOMapper;
    }

    @Override
    public UserResponseDTO map(UserEntity user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .secondLastName(user.getSecondLastName())
                .identificationType(this.identificationTypeEntityToIdentificationTypeResponseDTOMapper.map(user.getIdentificationType()))
                .identificationNumber(user.getIdentificationNumber())
                .email(user.getEmail())
                .role(this.roleEntityToRoleResponseDTOMapper.map(user.getRole()))
                .build();
    }

    @Override
    public List<UserResponseDTO> map(List<UserEntity> userList) {

        List<UserResponseDTO> response = new ArrayList<>();

        userList.forEach(user -> {
            response.add(
                    UserResponseDTO.builder()
                            .userId(user.getUserId())
                            .firstName(user.getFirstName())
                            .middleName(user.getMiddleName())
                            .lastName(user.getLastName())
                            .secondLastName(user.getSecondLastName())
                            .identificationType(this.identificationTypeEntityToIdentificationTypeResponseDTOMapper.map(user.getIdentificationType()))
                            .identificationNumber(user.getIdentificationNumber())
                            .email(user.getEmail())
                            .role(this.roleEntityToRoleResponseDTOMapper.map(user.getRole()))
                            .build()
            );
        });

        return response;

    }

}