package com.example.model;
 
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import lombok.*;
 
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer {
 
    @NotBlank(message = "Coupon code is required")
    private String couponCode;
 
    @NotBlank(message = "Offer description is required")
    @Column(name = "offer_description")
    private String description;
 
    @Min(value = 0, message = "Discount must be at least 0")
    @Max(value = 100, message = "Discount must not exceed 100")
    private int discountPercentage;
 
    private boolean active;
}