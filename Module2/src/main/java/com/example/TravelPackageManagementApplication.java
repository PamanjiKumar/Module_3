package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient //Enables Eureka client functionality
@EnableFeignClients
public class TravelPackageManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelPackageManagementApplication.class, args);
	}

}
