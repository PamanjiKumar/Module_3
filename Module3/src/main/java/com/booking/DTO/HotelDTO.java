package com.booking.DTO;

import lombok.Data;

@Data
public class HotelDTO {
	private String name;
	private String city;
	private double rating;
	private int nights;
	private double costPerNight;

}
