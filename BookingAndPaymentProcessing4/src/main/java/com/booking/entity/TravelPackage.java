package com.booking.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
 
@Entity
public class TravelPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destination;
    private String description;
    private double price;
    private String currency;
    private String offer;
    private LocalDate startDate;
    private LocalDate endDate;
    private String country;
 
    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
    	this.id = id;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
	public String getOffer() {
		return offer;
	}
	public void setOffer(String offer) {
		this.offer = offer;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}