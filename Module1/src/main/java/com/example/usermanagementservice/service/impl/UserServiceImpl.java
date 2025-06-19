package com.example.usermanagementservice.service.impl;
import com.example.usermanagementservice.model.User;

import jakarta.servlet.http.HttpServletRequest;
import com.example.usermanagementservice.repository.UserRepository;
import com.example.usermanagementservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.example.usermanagementservice.dto.TravelPackageDTO;
import com.example.usermanagementservice.dto.TravelPackageDTO;
import com.example.usermanagementservice.dto.UserDTO;
import com.example.usermanagementservice.dto.UserRoleCountResponse;
import com.example.usermanagementservice.exception.AdminRegistrationNotAllowedException;
import com.example.usermanagementservice.exception.EmailAlreadyExistsException;
import com.example.usermanagementservice.exception.PhoneNumberAlreadyExistsException;
import com.example.usermanagementservice.exception.UserNotFoundException;
import com.example.usermanagementservice.feign.TravelPackageClient;

import java.util.List;
//this class implements the logic defined in Userservice
@Service
public class UserServiceImpl implements UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired 
	private TravelPackageClient travelPackageClient;
	
	//Register a new user
	@Override
	public User registerUser(User user) {
		logger.info("Registering user with email: {}",user.getEmail());
		
		// check if email already exists
		if (userRepository.findByEmail(user.getEmail()).isPresent() ) {
			//Don't continue if already exists
			throw new EmailAlreadyExistsException("Email already registered");
		}
		
		// check if number already exists
		if(userRepository.findByContactNumber(user.getContactNumber()).isPresent()) {
			throw new PhoneNumberAlreadyExistsException("Phone number already registered");
		}
		//prevent users from registering as admin
		if("ADMIN".equalsIgnoreCase(user.getRole())) {
			throw new AdminRegistrationNotAllowedException("You are not allowed to register as ADMIN");
		}
		
		//Proceed to save
		return userRepository.save(user); //Save user to DB
	}
	
	// Get all users
	@Override
	public List<User> getAllUsers() {
		logger.info("Fetching all users...");
		return userRepository.findAll();
	}
	
	//Find user by email
	@Override
	public User getUserByEmail(String email) {
	logger.info("Looking for user with email: {}", email);
	 
	    return userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
	}

	
	//Delete user by ID
	@Override
	public void deleteUser(Long id) {
		logger.info("Deleting user with ID: {}", id);
		
		if(! userRepository.existsById(id)) {
			throw new UserNotFoundException("User not found with ID: " + id);
		}
		userRepository.deleteById(id);
	}

	//view user by ID(profile view)
	@Override
	public User getUserById(Long id) {
		logger.info("Fetching profile for user ID: {}", id);
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: "+ id));
	}

	//update user profile
	@Override
	public User updateUserProfile(Long id, User updatedUser) {
		logger.info("Updating profile for user with ID: {}", id);
		//First, get existing user
		User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
		
		
		//Update fields
		existingUser.setName(updatedUser.getName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setPassword(updatedUser.getPassword());
		existingUser.setRole(updatedUser.getRole());
		existingUser.setContactNumber(updatedUser.getContactNumber());
		
		//save changes
		
		return userRepository.save(existingUser);
	}
	
	//to get number of users, agents and customers
	@Override
	public UserRoleCountResponse getUserRoleCounts() {
		long total = userRepository.count()-1;
		long agents = userRepository.countByRole("AGENT");
		long customers = userRepository.countByRole("CUSTOMER");
		
		logger.info("Fetched user role counts: total={}, agents={}, customers={}", total, agents, customers);
		
		return new UserRoleCountResponse(total, agents, customers);
	}
	
	//to get all the created packages of an agent
	@Override
    public List<TravelPackageDTO> fetchAllPackagesByAgent(Long agentId) {
        // ✅ Pass `id` as `agentId`
        return travelPackageClient.getPackagesByAgent(agentId);
    }

	
	
	
	//convert user to userDTO for the user data transfer to other services
	public UserDTO convertToDTO(User user) {
	    return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
	}
	
	

}
