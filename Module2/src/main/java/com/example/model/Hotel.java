package com.example.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank(message = "Hotel name is required")
    private String name;
 
    @NotBlank(message = "City is required")
    private String city;
 
    @DecimalMin(value = "0.0", inclusive = false, message = "Rating must be greater than 0")
    private double rating;
 
    @Min(value = 1, message = "Number of nights must be at least 1")
    private int nights;
 
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost per night must be greater than 0")
    private double costPerNight;
}
 