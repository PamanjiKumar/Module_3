package com.example.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank(message = "Airline name is required")
    private String airline;
 
    @NotBlank(message = "From city is required")
    private String fromCity;
 
    @NotBlank(message = "To city is required")
    private String toCity;
 
    @NotBlank(message = "Departure time is required")
    private String departureTime;
 
    @NotBlank(message = "Arrival time is required")
    private String arrivalTime;
}
 