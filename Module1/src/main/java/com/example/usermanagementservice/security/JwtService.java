package com.example.usermanagementservice.security;
 
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
 
import java.security.Key;
import java.util.Date;


 
@Service
public class JwtService {
 
// Inject from application.properties
    @Value("${jwt.secret}")
    private String secret;
 
    private final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour
 
    // Generate token using dynamic key from injected secret
    public String generateToken(String email, String role) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
 
        
        
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256) 
                .compact();
        
       
    }
 
    // Provide key for validation (used by filters/controllers)
    public Key getSecretKey() {
    	
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    
}