package com.example.LibraryManagement.controller;

import com.example.LibraryManagement.dto.LoginRequestDTO;
import com.example.LibraryManagement.dto.LoginResponseDTO;
import com.example.LibraryManagement.dto.RegisterRequestDTO;
import com.example.LibraryManagement.entity.User;
import com.example.LibraryManagement.service.AuthenticatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private AuthenticatService authenticatService;

    @PostMapping("/registernormaluser")
    public ResponseEntity<User> registerNormalUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authenticatService.registerNormalUser(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authenticatService.login(loginRequestDTO));
    }



}
