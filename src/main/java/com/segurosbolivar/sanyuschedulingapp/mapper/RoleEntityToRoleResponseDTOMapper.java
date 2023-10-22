package com.segurosbolivar.sanyuschedulingapp.mapper;

import com.segurosbolivar.sanyuschedulingapp.dto.RoleResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleEntityToRoleResponseDTOMapper implements IMapper<RoleEntity, RoleResponseDTO> {

    @Override
    public RoleResponseDTO map(RoleEntity role) {
        return RoleResponseDTO.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

    @Override
    public List<RoleResponseDTO> map(List<RoleEntity> roleList) {

        ArrayList<RoleResponseDTO> response = new ArrayList<>();

        for (RoleEntity role: roleList) {
            response.add(
                    RoleResponseDTO.builder()
                            .roleId(role.getRoleId())
                            .name(role.getName())
                            .description(role.getDescription())
                            .build()
            );
        }

        return response;

    }

}