package com.booking.service;

import com.booking.client.TravelPackageClient;
import com.booking.client.UserClient;
import com.booking.dto.BookingDTO;
import com.booking.dto.TravelPackageDTO;
import com.booking.dto.UserDTO;
import com.booking.entity.Booking;
import com.booking.repository.BookingRepository;
//import com.booking.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private TravelPackageClient travelPackageClient;
    @Autowired
    private UserClient userClient;
    private static final Logger logger = Logger.getLogger(BookingService.class.getName());
    public BookingDTO createBooking(Booking bookingRequest) {
        Long userId = bookingRequest.getUserId();
        Long packageId = bookingRequest.getPackageId();
logger.info("Creating booking for userId: " + userId + " and packageId: " + packageId);
        // Validate User
        UserDTO user = userClient.getCustomerById(userId);
        if (user == null || !"CUSTOMER".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("User is not a valid CUSTOMER.");
        }
        // Validate Package
        TravelPackageDTO travelPackage = travelPackageClient.getPackageById(packageId);
        if (travelPackage == null) {
            throw new IllegalArgumentException("Invalid travel package ID.");
        }
 
        // Create and save booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setPackageId(packageId);
        booking.setTripStartDate(travelPackage.getTripStartDate());
        booking.setTripEndDate(travelPackage.getTripEndDate());
        booking.setStatus("CONFIRMED");
        Booking savedBooking = bookingRepo.save(booking);
        // Build response DTO
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(savedBooking.getBookingId());
        dto.setUserId(userId);
        dto.setPackageId(packageId);
        dto.setTripStartDate(savedBooking.getTripStartDate());
        dto.setTripEndDate(savedBooking.getTripEndDate());
        dto.setStatus(savedBooking.getStatus());
        dto.setPaymentId(savedBooking.getPaymentId());
logger.info("Booking created successfully with bookingId: " + savedBooking.getBookingId());
        return dto;
    }
    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }
    
    public Booking getBookingById(Long id) {
        return bookingRepo.findById(id).orElse(null);
    }
    
    public List<Booking> getAllBookingsByUserId(Long userId) {
    	return bookingRepo.findByUserId(userId);
    }
    
    public Booking getInternalBookingById(Long id) {
        return bookingRepo.findById(id).orElse(null);
    }
    
    public void deleteBooking(Long id) {
        bookingRepo.deleteById(id);
    }
    
    public ResponseEntity<String> cancelBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking == null) {
            return ResponseEntity.badRequest().body("Booking not found.");
        }
        LocalDate today = LocalDate.now();
        if (booking.getTripStartDate().minusDays(7).isBefore(today)) {
            return ResponseEntity.badRequest().body("Cancellation not allowed. Must cancel at least 7 days before departure.");
        }
        booking.setStatus("CANCELLED");
        bookingRepo.save(booking);
        return ResponseEntity.ok("Booking cancelled successfully.");
    }
 
    // Rating and reviews module to validate booking
    public boolean hasUserCompletedPackage(Long userId, String packageId) {
        List<Booking> bookings = bookingRepo.findByUserId(userId);
        LocalDate today = LocalDate.now();
        return bookings.stream()
            .anyMatch(b ->
                b.getPackageId().equals(packageId) &&
                "CONFIRMED".equalsIgnoreCase(b.getStatus()) &&
                b.getTripEndDate().isBefore(today)
            );
    }
}