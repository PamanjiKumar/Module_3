//All the business logic like Registering, getting, updating, deleting a user
package com.example.usermanagementservice.service;
import com.example.usermanagementservice.dto.UserRoleCountResponse;
//this interface defines what functions are available
import com.example.usermanagementservice.model.User;

import jakarta.servlet.http.HttpServletRequest;

//import com.example.usermanagementservice.dto.PackageDTO;
import com.example.usermanagementservice.dto.TravelPackageDTO;
import com.example.usermanagementservice.dto.UserDTO;

import java.util.List;

public interface UserService {
	User registerUser(User user); //Add new user
	List<User> getAllUsers(); // Get all users
	User getUserByEmail(String email); //find user by email
	
	void deleteUser(Long id); //deletes user
	User getUserById(Long id); //View profile
	User updateUserProfile(Long id, User updatedUser);//Update profile like user management in their profile for editing profiles
	
	//get user count and count by roles as well
	UserRoleCountResponse getUserRoleCounts();
	
	//get all packages created by clients
	List<TravelPackageDTO> fetchAllPackagesByAgent(Long id);

	UserDTO convertToDTO(User user);
}
