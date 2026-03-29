package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import com.distributedstorage.backend.dto.LoginRequestDTO;
import com.distributedstorage.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/auth")
public class AuthController {

    // AuthService handles login logic and token generation
    private final AuthService authService;

    // Constructor injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /auth/login — validates credentials and returns JWT token
    @PostMapping("/login")
    public ApiResponseDTO<String> login(@RequestBody LoginRequestDTO dto) {

        // Call service to validate credentials and get token
        String token = authService.login(dto);

        // Return token in standard API response wrapper
        return new ApiResponseDTO<>(
                "SUCCESS",
                "Login successful",
                token
        );
    }
}