package com.booking.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class PackageDTO {
	private Long packageId;
	//private Long agentId;
	private String title;
	private String description;
	private int duration;
	private double price;
	private int maxCapacity;
	private LocalDate tripStartDate;
	private LocalDate tripEndDate;

}
