package com.example.dto;

import lombok.Data;

@Data
public class FlightDTO {
	private String airline;
	private String fromCity;
	private String toCity;
	private String departureTime;
	private String arrivalTime;

}
