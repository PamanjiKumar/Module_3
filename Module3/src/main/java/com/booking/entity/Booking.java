package com.booking.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private Long userId;
    private Long packageId;
    
    @Column(name= "trip_start_date")
    private LocalDate tripStartDate;
    
    @Column(name= "trip_end_date")
    private LocalDate tripEndDate;
    
    private String status;
    private Long paymentId;

    // Getters and Setters
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
    
	public LocalDate getTripStartDate() {
		return tripStartDate;
	}

	public void setTripStartDate(LocalDate tripStartDate) {
		this.tripStartDate = tripStartDate;
	}

	public LocalDate getTripEndDate() {
		return tripEndDate;
	}

	public void setTripEndDate(LocalDate tripEndDate) {
		this.tripEndDate = tripEndDate;
	}
	
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

}
