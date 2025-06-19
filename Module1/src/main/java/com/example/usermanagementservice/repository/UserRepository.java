package com.example.usermanagementservice.repository;

import com.example.usermanagementservice.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This interface gives us all basic DB operations for the User entity
@Repository
//this tells spring to work with user entity and primary key is long
public interface UserRepository extends JpaRepository<User, Long>{
	// we can add custom methods here if needed
	//to find user by email
	Optional<User> findByEmail(String email);
	Optional<User> findByContactNumber(String contactNumber);
	
	//Total users
	long count();
	//Count by role(Agent, customer)
	long countByRole(String role);
}
