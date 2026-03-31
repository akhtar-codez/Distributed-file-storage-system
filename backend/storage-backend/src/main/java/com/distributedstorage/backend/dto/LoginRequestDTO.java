package com.distributedstorage.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class LoginRequestDTO {
    
    // Email used as the unique identifier for login 
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Plain  text password - will be verified against Bcrypt hash in database 
    @NotBlank(message = "Password is required")
    private String password;

    public String getEmail(){
        return email;
    }
    
    public String getPassword(){
        return password;
    }
}
