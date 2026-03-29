package com.distributedstorage.backend.service;

import com.distributedstorage.backend.dto.UserRegistrationDTO;
import com.distributedstorage.backend.dto.UserResponseDTO;
import com.distributedstorage.backend.model.FileMetadata;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.repository.FMDRepository;
import com.distributedstorage.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    // Repository for user database operations
    private final UserRepository userRepository;

    // Repository for file metadata — needed to delete user's files before deleting user
    private final FMDRepository fmdRepository;

    // PasswordEncoder — used to hash passwords before saving to database
    private final PasswordEncoder passwordEncoder;

    // Constructor injection — Spring automatically provides both repositories
    public UserService(UserRepository userRepository, FMDRepository fmdRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.fmdRepository = fmdRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registers a new user after checking for duplicate email
    public User registerUser(UserRegistrationDTO dto) {

        // Reject registration if email already exists
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("Email already registered");
                });

        // Build new user object from request data
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        
        // Hash password using BCrypt before saving — never store plain text passwords
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    // Returns raw User entity — used internally by other services
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Returns clean UserResponseDTO — used by API endpoints (no password exposed)
    public UserResponseDTO getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    // Deletes a user and all their associated files
    public void deleteUser(Long id) {

        // Check if user exists before attempting delete
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete all files belonging to this user first
        // Required to avoid foreign key constraint violation in MySQL
        List<FileMetadata> files = fmdRepository.findByUserId(id);
        fmdRepository.deleteAll(files);

        // Now safe to delete the user
        userRepository.delete(user);
    }
}