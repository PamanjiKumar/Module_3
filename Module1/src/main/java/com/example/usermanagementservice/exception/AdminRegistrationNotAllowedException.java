package com.example.usermanagementservice.exception;

public class AdminRegistrationNotAllowedException extends RuntimeException {
	public AdminRegistrationNotAllowedException(String message) {
        super(message);  // This allows getMessage() to work
    }
}
