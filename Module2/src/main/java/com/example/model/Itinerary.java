package com.example.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Itinerary {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Min(value = 1, message = "Day number must be at least 1")
    private int dayNumber;
 
    @NotBlank(message = "Activity title is required")
    private String activityTitle;
 
    @NotBlank(message = "Activity description is required")
    private String activityDescription;
}
 