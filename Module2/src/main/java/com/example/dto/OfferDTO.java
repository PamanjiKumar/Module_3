package com.example.dto;

import lombok.Data;

@Data
public class OfferDTO {
	private String couponCode;
	private String description;
	private int discountPercentage;
	private boolean active;

}
