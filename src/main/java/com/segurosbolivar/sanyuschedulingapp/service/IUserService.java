package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.response.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IUserService {

    UserResponseDTO findByUserId(Long userId);
    UserResponseDTO findByEmail(String email);
    List<UserResponseDTO> findAvailableContractorsByRoleDateRange(String roleName, LocalDateTime startDate, LocalDateTime endDate);

}