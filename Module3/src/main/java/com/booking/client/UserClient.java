package com.booking.client;

import com.booking.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usermanagement-service")
public interface UserClient {

    @GetMapping("/api/users/internal/{id}")
    UserDTO getUserById(@PathVariable("id") Long userId);
}