package com.booking.service;

import com.booking.client.TravelPackageClient;
import com.booking.client.UserClient;
import com.booking.DTO.BookingDTO;
import com.booking.DTO.TravelPackageDTO;
import com.booking.DTO.UserDTO;
import com.booking.entity.Booking;
import com.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private TravelPackageClient travelPackageClient;

    @Autowired
    private UserClient userClient;

    public BookingDTO createBooking(Booking booking) {
        TravelPackageDTO travelPackage = travelPackageClient.getPackageById(booking.getPackageId());
        UserDTO user = userClient.getUserById(booking.getUserId());

        if (travelPackage == null || user == null) {
            throw new RuntimeException("Invalid travel package or user");
        }

        booking.setTripStartDate(travelPackage.getTripStartDate());
        booking.setTripEndDate(travelPackage.getTripEndDate());
        booking.setStatus("CONFIRMED");

        Booking savedBooking = bookingRepo.save(booking);

        BookingDTO dto = new BookingDTO();
        dto.setBookingId(savedBooking.getBookingId());
        dto.setUserId(savedBooking.getUserId());
        dto.setPackageId(savedBooking.getPackageId());
        dto.setTripStartDate(savedBooking.getTripStartDate());
        dto.setTripEndDate(savedBooking.getTripEndDate());
        dto.setStatus(savedBooking.getStatus());
        dto.setPaymentId(savedBooking.getPaymentId());

        return dto;
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public Booking getBookingById(Long id) {
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
}