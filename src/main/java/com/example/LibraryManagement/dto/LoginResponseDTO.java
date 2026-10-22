package com.example.LibraryManagement.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder

public class LoginResponseDTO {

    private String token;
    private String userName;
    private Set<String> roles;


}
