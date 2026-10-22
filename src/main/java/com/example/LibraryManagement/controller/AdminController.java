package com.example.LibraryManagement.controller;

import com.example.LibraryManagement.dto.RegisterRequestDTO;
import com.example.LibraryManagement.entity.User;
import com.example.LibraryManagement.service.AuthenticatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")

//the first admin must be registered using the sql script .

public class AdminController {

    @Autowired
    private AuthenticatService authenticatService;

    @PostMapping("/registeradminuser")
    public ResponseEntity<User> registerAdminUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authenticatService.registerAdminUser(registerRequestDTO));
    }
}
