package com.grupo5.tpo.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
