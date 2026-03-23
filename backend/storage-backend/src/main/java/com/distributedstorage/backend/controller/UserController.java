package com.distributedstorage.backend.controller;

import com.distributedstorage.backend.model.User;
import com.distributedstorage.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {

        // Fetch user from service
        User user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    // DELETE user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        // Delete user
        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted successfully");
    }
}