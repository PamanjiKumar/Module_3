package com.example.usermanagementservice.dto;
 
public class LoginResponse {
    private String token;
    private String role;
 
    public LoginResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }
 
    // Getters only
    public String getToken() {
        return token;
    }
 
    public String getRole() {
        return role;
    }
}
 