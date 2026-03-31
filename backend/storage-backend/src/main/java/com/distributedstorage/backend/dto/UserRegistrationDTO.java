package com.distributedstorage.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {

    // Username must not be empty
    @NotBlank(message = "Username is required")
    private String username;

    // Must be a valid email format
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Password must be at least 6 characters
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}