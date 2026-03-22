package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.dto.ApiResponseDTO;
import com.distributedstorage.backend.dto.FileUploadResponseDTO;
import com.distributedstorage.backend.dto.UserRegistrationDTO;
// import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.service.FileService;
import com.distributedstorage.backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ApiResponseDTO<String> registerUser(@RequestBody UserRegistrationDTO dto) {
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
}