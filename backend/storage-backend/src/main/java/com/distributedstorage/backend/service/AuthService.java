package com.distributedstorage.backend.service;

import com.distributedstorage.backend.dto.LoginRequestDTO;
import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.repository.UserRepository;
import com.distributedstorage.backend.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Repository to fetch user by email during login
    private final UserRepository userRepository;

    // PasswordEncoder to verify plain text password against BCrypt hash
    private final PasswordEncoder passwordEncoder;

    // JwtUtil to generate JWT token after successful login
    private final JwtUtil jwtUtil;

    // Constructor injection — Spring provides all dependencies automatically
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Validates credentials and returns JWT token on success
    public String login(LoginRequestDTO dto) {

        // Step 1 — Find user by email, throw error if not found
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Step 2 — Verify plain text password against stored BCrypt hash
        // If password doesn't match, throw generic error (don't reveal which field is wrong)
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Step 3 — Generate and return JWT token containing user's email
        return jwtUtil.generateToken(user.getEmail());
    }
}