package com.booking.client;
 
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
import com.booking.dto.UserDTO;
 
@FeignClient(name = "usermanagement-service")
public interface UserClient {
 
    @GetMapping("/api/users/internal/customer/{id}")
    UserDTO getCustomerById(@PathVariable("id") Long userId);
 
	//UserDTO getCustomerById(Long userId);

}