package com.example.usermanagementservice.controller;
 

import com.example.usermanagementservice.dto.TravelPackageDTO;
import com.example.usermanagementservice.dto.UserDTO;
import com.example.usermanagementservice.dto.UserRoleCountResponse;
import com.example.usermanagementservice.exception.RoleChangeNotAllowedException;
import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.service.UserService;
import io.jsonwebtoken.Jwts;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.usermanagementservice.security.*;

import java.util.Collections;
import java.util.List;
 
@RestController
@RequestMapping("/api/users")
public class UserController {
 
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtService jwtService;
 
    
    // Helper method to extract role from JWT token in Authorization header
    private String extractRoleFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return Jwts.parserBuilder()
.setSigningKey(jwtService.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);
        }
        return null;
    }
 

    /**
     * Register new user
     * @param user the user object containing register details
     * @return a ResponseEntity containing user details
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }
 
    /**
     * Get all users - only ADMIN can access
     * @param request HttpServeletRequest to extract role from JWT
     * @return ResponseEntity containing list of users if ADMIN, otherwise 403 FORBIDDEN
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        String role = extractRoleFromHeader(request); //Extract role from jwt
        
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Only ADMINs can view all users");
        }
        List<User> users = userService.getAllUsers();
        System.out.println("Role from token = " + role);
        return ResponseEntity.ok(users);
    }
 
    // Get user by ID - only ADMIN 
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, HttpServletRequest request) {
        
        String role = extractRoleFromHeader(request);
 
        User user = userService.getUserById(id);
 
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(user);
    }
 
    // Update user profile - only admin
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody User updatedUser, HttpServletRequest request) {
        
        String role = extractRoleFromHeader(request);
 
 
        // Only ADMIN 
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: You can only update your own profile");
        }
 
        User user = userService.updateUserProfile(id, updatedUser);
        return ResponseEntity.ok(user);
    }
 
    // Delete user - only ADMIN can delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        String role = extractRoleFromHeader(request);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMINs can delete users.");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted.");
    }
    
    //get total count of users, agents, customers
    @GetMapping("/counts")
    public ResponseEntity<?> getUserRoleCounts(HttpServletRequest request) {
        String role = extractRoleFromHeader(request);
        if (!"ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                 .body(Collections.singletonMap("message", "Only admins can view user counts."));
        }
     
        UserRoleCountResponse response = userService.getUserRoleCounts();
        return ResponseEntity.ok(response);
    }
    
    
    // myprofile endpoint for users(only)
    //get their profile details
    @GetMapping("/myprofile")
    public ResponseEntity<?> getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
     
        return ResponseEntity.ok(user);
    }
     //update their own profile details
    @PutMapping("/myprofile")
    public ResponseEntity<?> updateMyProfile(@RequestBody User updatedUser) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User existingUser = userService.getUserByEmail(email);
     
        // Add role-protection logic here
        if (!"ADMIN".equalsIgnoreCase(existingUser.getRole()) &&
            "ADMIN".equalsIgnoreCase(updatedUser.getRole())) {
            throw new RoleChangeNotAllowedException("You cannot assign yourself ADMIN role.");
        }
     
        User user = userService.updateUserProfile(existingUser.getId(), updatedUser);
        return ResponseEntity.ok(user);
    }
     
     //delete their own profile
    @DeleteMapping("/myprofile")
    public ResponseEntity<?> deleteMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);
        userService.deleteUser(user.getId());
     
        return ResponseEntity.ok(Collections.singletonMap("message", "Your profile has been deleted."));
    }
    
    /*---------------------------------*/
    
	// Internal endpoint for microservices (e.g., Travel Package Service)
	@GetMapping("/internal/{id}")
	public ResponseEntity<?> getUserForInternalUse(@PathVariable Long id) {
		User user = userService.getUserById(id); // No security check, get user from DB
		
		// ✅ Check if user exists
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body(Collections.singletonMap("message", "User not found with ID: " + id));
	    }
	 
	    // ✅ Check if user is an AGENT
	    if (!"AGENT".equalsIgnoreCase(user.getRole())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN)
	                .body(Collections.singletonMap("message", "User with ID " + id + " is not an AGENT"));
	    }
		
		UserDTO userDTO = userService.convertToDTO(user);
		return ResponseEntity.ok(userDTO);
	}
	
	
    
    //To fetch all the packages under a travel agent
	@GetMapping("/packages/{id}")
    public ResponseEntity<?> getAllPackagesOfAgent(@PathVariable Long id) {
        List<TravelPackageDTO> packages = userService.fetchAllPackagesByAgent(id); 
        return ResponseEntity.ok(packages);
    }

}