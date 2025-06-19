package com.example.usermanagementservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


// this class represents a user in the system
@Entity
@Table(name = "users") //set table name as 'users'

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)// Auto-increment primary key
	private Long id;
	
	@Column(nullable=false)
	private String name; //User's name
	
	@Email(message ="Invalid email format")
	@NotBlank(message="Email is required")
	@Column(nullable=false, unique=true)
	private String email; //Email must be unique
	
	@NotBlank(message="Password is required")
	@Size(min = 6, message = "Password must be atleast 6 characters")
	@Column(nullable=false)
	private String password; //User's password
	
	@Column(nullable=false)
	private String role; // Role: AGENT or CUSTOMER
	
	
	@NotBlank(message="Contact number is required")
	@Pattern(regexp = "\\d{10}", message = "Contact number must be 10 digits")
	@Column(name ="contact_number",nullable=false)
	private String contactNumber; // phone number of the user

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
	
	
	

}
