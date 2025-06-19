package com.example.usermanagementservice.exception;
 
// Must extend RuntimeException or Exception
public class UserNotFoundException extends RuntimeException {
 
    public UserNotFoundException(String message) {
        super(message);  // This allows getMessage() to work
    }
}