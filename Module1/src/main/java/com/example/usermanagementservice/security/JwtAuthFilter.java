package com.example.usermanagementservice.security;
 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import java.io.IOException;

import java.util.Collections;
 
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
 
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
 
    @Autowired
    private JwtService jwtService;
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
 
        String authHeader = request.getHeader("Authorization");
 
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
 
            try {
 
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtService.getSecretKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
 
                String email = claims.getSubject();
                String role = claims.get("role", String.class);
 
logger.info("Token validated for email: {} with role: {}", email, role);
 
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
 
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
 
            } catch (Exception e) {
                logger.warn("❌ Invalid or expired JWT: {}", e.getMessage());
                
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"message\":\"You have been logged out or session expired\"}");
                return; //Don't continue filter chain
            }
        }
 
        filterChain.doFilter(request, response);
    }
}