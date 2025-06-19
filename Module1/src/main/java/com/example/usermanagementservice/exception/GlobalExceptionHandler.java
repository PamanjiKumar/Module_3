package com.example.usermanagementservice.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 
import java.util.Collections;
import java.util.stream.Collectors;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
 
import java.util.Collections;

//ControllerAdvice + ResponseBody
@RestControllerAdvice //intercepts exceptions thrown by any restcontroller in the app, centralises error handling logic
public class GlobalExceptionHandler {
	
	//if email already exists in db during registration
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) //409
                .body(Collections.singletonMap("message", ex.getMessage()));
    }
 
  //if number already exists in db during registration
    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<?> handlePhoneNumberExists(PhoneNumberAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) //409
                .body(Collections.singletonMap("message", ex.getMessage()));
    }
    
    //if user not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)  // This sends 404
                .body(Collections.singletonMap("message", ex.getMessage()));
    }
    
    //validation for email and phone number format
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Collect all validation error messages (e.g. "email: Invalid format")
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
     
        // Return a clean JSON response
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) //403
                .body(Collections.singletonMap("message", message));
    }
    //if user tries to register as admin
    @ExceptionHandler(AdminRegistrationNotAllowedException.class)
    public ResponseEntity<?> handleAdminRegisterBlocked(AdminRegistrationNotAllowedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap("message", ex.getMessage()));
    }
    
    //if non admin user tries to update their role
    @ExceptionHandler(RoleChangeNotAllowedException.class)
    public ResponseEntity<?> handleRoleChange(RoleChangeNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap("message", ex.getMessage()));
    }
    
   
     
}