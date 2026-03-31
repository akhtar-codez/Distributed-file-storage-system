package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import com.distributedstorage.backend.dto.FileUploadResponseDTO;
import com.distributedstorage.backend.dto.UserRegistrationDTO;
import com.distributedstorage.backend.dto.UserResponseDTO;
// import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.service.FileService;
import com.distributedstorage.backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FileService fileService;

    public UserController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/register")
    public ApiResponseDTO<String> registerUser(@Valid @RequestBody UserRegistrationDTO dto) {
        User user = userService.registerUser(dto);
        return new ApiResponseDTO<>(
                "SUCCESS",
                "User registered successfully",
                "User ID: " + user.getId()
        );
    }

   @GetMapping("/{userId}/files")
    public ApiResponseDTO<List<FileUploadResponseDTO>> getFilesByUser(@PathVariable Long userId) {
        List<FileUploadResponseDTO> files = fileService.getFilesByUser(userId);
        return new ApiResponseDTO<>("SUCCESS", "Files fetched successfully", files);
    }

    @GetMapping("/{id}")
    public ApiResponseDTO<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserProfile(id);
        return new ApiResponseDTO<>("SUCCESS", "User fetched successfully", user);
    }
    
    // Deletes a user and all their associated files
    // Returns success message on deletion
    @DeleteMapping("/{id}")
    public ApiResponseDTO<String> deleteUser(@PathVariable Long id) {
        
        // Call service to handle deletion logic
        userService.deleteUser(id);
        
        return new ApiResponseDTO<>(
                "SUCCESS",
                "User deleted successfully",
                null
        );
    }
}