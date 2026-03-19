package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import com.distributedstorage.backend.dto.UserRegistrationDTO;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponseDTO<String> registerUser(@RequestBody UserRegistrationDTO dto) {

        User user = userService.registerUser(dto);

        return new ApiResponseDTO<>(
                "SUCCESS",
                "User registered successfully",
                "User ID: " + user.getId()
        );
    }
}
