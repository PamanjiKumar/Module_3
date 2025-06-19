package com.example.model;
 
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
 
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelPackage {
 
    

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="package_id")
    private Long packageId;
	
	@NotNull(message="Agent ID is required")
	private Long agentId;
 
    @NotBlank(message = "Title is mandatory")
    private String title;
 
    @NotBlank(message = "Description is mandatory")
    private String description;
 
    @Min(value = 1, message = "Duration should be at least 1 day")
    private int duration;
 
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private double price;
    
    @Min(value=1, message="MAX capacity should be atleast 1")
    @Column(name="max_capacity", nullable=false)
    private int maxCapacity;
 
    @NotNull(message="Trip start date is required")
    private LocalDate tripStartDate;
    
    @NotNull(message="Trip end date is required")
    private LocalDate tripEndDate;
    
    
    @ElementCollection
    @CollectionTable(name="travel_package_highlights", joinColumns= @JoinColumn(name="travel_package_package_id"))
    @Column(name="highlight")
    @NotNull(message = "Highlights cannot be null")
    private List<@NotBlank(message = "Highlight item cannot be blank") String> highlights;
 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="travel_package_id")
    @NotNull(message = "Flights must not be null")
    private List<@Valid Flight> flights;
 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="travel_package_id")
    @NotNull(message = "Hotels must not be null")
    private List<@Valid Hotel> hotels;
 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="travel_package_id")
    @NotNull(message = "Sightseeing must not be null")
    private List<@Valid Sightseeing> sightseeing;
 
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name="travel_package_id")
    @NotNull(message = "Itinerary must not be null")
    private List<@Valid Itinerary> itinerary;
 
    @Embedded
    @Valid
    private Offer offer;
    
    //new things
    
    @NotBlank(message = "Country is required")
    private String country;
    
    @NotBlank(message = "destination is required")
    private String destination;
    
    @NotBlank(message = "trip type is required")
    private String tripType;
    
    
    
    

	
}
 