package com.example.usermanagementservice.exception;
 
// Must extend RuntimeException or Exception
public class EmailAlreadyExistsException extends RuntimeException {
 
    public EmailAlreadyExistsException(String message) {
        super(message);  // This allows getMessage() to work
    }
}