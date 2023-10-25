package com.segurosbolivar.sanyuschedulingapp.controller;

import com.segurosbolivar.sanyuschedulingapp.dto.response.UserResponseDTO;
import com.segurosbolivar.sanyuschedulingapp.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('Administrador', 'Contratista Supervisor', 'Contratista de Campo')")
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> findUserByEmail(@PathVariable  String email) {
        UserResponseDTO user = this.userService.findByEmail(email);
        return new ResponseEntity<UserResponseDTO>(user, HttpStatus.OK);
    }

}