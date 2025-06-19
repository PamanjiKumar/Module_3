package com.example.usermanagementservice.controller;
 
import com.example.usermanagementservice.dto.LoginRequest;
import com.example.usermanagementservice.dto.LoginResponse;
import com.example.usermanagementservice.exception.UserNotFoundException;
import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.repository.UserRepository;
import com.example.usermanagementservice.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/api/auth")  // Base URL: /api/auth
public class AuthController {
 
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
 
    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private JwtService jwtService;
 
    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
logger.info("Login attempt for email: {}", request.getEmail());
 
        // 1. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
        		.orElseThrow(() -> new UserNotFoundException("User not Found with email: " + request.getEmail()));
 
        // 2. If user not found or password doesn't match, return error
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            logger.warn("Invalid login for email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid email or password"));
        }
 
        // 3. Generate JWT token with user's email and role
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
 
logger.info("Login successful for email: {} with role: {}", user.getEmail(), user.getRole());
 
        // 4. Return token + role in response
        return ResponseEntity.ok(new LoginResponse(token, user.getRole()));
    }
    
    //log out endpoint
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
     
    logger.info("Logout attempt triggered by user");
     
        
        return ResponseEntity.ok(Collections.singletonMap("message", "Logout successful"));
    }
    
}