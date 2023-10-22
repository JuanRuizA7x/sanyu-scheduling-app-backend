package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.dto.UserResponseDTO;

public interface IUserService {

    UserResponseDTO findUserByEmail(String email);

}