package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.LoginRequestDTO;
import com.example.LibraryManagement.dto.LoginResponseDTO;
import com.example.LibraryManagement.dto.RegisterRequestDTO;
import com.example.LibraryManagement.entity.User;
import com.example.LibraryManagement.jwtauthentication.JWTService;
import com.example.LibraryManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.Set;

@Service

public class AuthenticatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User registerNormalUser(RegisterRequestDTO  registerRequestDTO) {

        if(userRepository.findByName(registerRequestDTO.getUserName()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        Set<String> roles =  new HashSet<String>();
        roles.add("ROLE_USER");

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setName(registerRequestDTO.getUserName());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        return userRepository.save(user);

    }

    public User registerAdminUser(RegisterRequestDTO registerRequestDTO) {

        if(userRepository.findByName(registerRequestDTO.getUserName()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        Set<String> roles =  new HashSet<String>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setName(registerRequestDTO.getUserName());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        return userRepository.save(user);

    }

    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUserName(),
                        loginRequestDTO.getPassword()
                )
        );

        User user = userRepository.findByName(loginRequestDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("Username not found"));

        String token = jwtService.generateToken(user);

        return LoginResponseDTO.builder()
                .token(token)
                .userName(user.getName())
                .roles(user.getRoles())
                .build();

    }

}
