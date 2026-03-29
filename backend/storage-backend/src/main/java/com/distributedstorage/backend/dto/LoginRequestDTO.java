package com.distributedstorage.backend.dto;

public class LoginRequestDTO {
    
    // Email used as the unique identifier for login 
    private String email;

    // Plain  text password - will be verified against Bcrypt hash in database 
    private String password;

    public String getEmail(){
        return email;
    }
    
    public String getPassword(){
        return password;
    }
}
