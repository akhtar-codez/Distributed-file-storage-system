package com.distributedstorage.backend.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor injection
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Fetch user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Delete user by ID
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // Register user with encrypted password
    public User registerUser(User user) {

        // Check duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Check password not empty
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
}