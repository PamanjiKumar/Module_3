package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.dto.UserDTO;



@FeignClient(name="USERMANAGEMENT-SERVICE")
public interface UserClient {
	
	@GetMapping("/api/users/internal/{id}")
	UserDTO getUserById(@PathVariable("id") Long agentid);

}
