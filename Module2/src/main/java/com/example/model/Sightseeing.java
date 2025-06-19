package com.example.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sightseeing {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank(message = "Sightseeing location is required")
    private String location;
 
    @NotBlank(message = "Sightseeing description is required")
    @Column(name = "sightseeing_description")
    private String description;
}
 