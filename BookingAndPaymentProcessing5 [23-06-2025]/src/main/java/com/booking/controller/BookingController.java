package com.booking.controller;
 
import com.booking.dto.BookingDTO;
import com.booking.entity.Booking;
import com.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.logging.Logger;
 
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
 
    private final BookingService service;
    private static final Logger logger = Logger.getLogger(BookingController.class.getName());
 
    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }
 
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody Booking booking) {
        try {
            BookingDTO bookingDTO = service.createBooking(booking);
            return new ResponseEntity<>(bookingDTO, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            logger.severe("Error creating booking: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
 
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = service.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = service.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }
    
 
    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        return service.cancelBooking(id);
    }
 
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteBooking(id);
    }
    
    // User Module to get bookings by User ID
    @GetMapping("/internal/bookings/user/{userId}")
    public ResponseEntity<List<Booking>> getAllBookingsByUserId(@PathVariable Long userId) {
    	List<Booking> bookings = service.getAllBookingsByUserId(userId);
    	if (bookings.isEmpty()) {
    		return ResponseEntity.notFound().build();
    		}
    	return ResponseEntity.ok(bookings);
    	}

    
    //  Reviews and rating to validate booking
    @GetMapping("/user/{userId}/package/{packageId}/completed")
    public boolean hasUserCompletedPackage(@PathVariable Long userId, @PathVariable String packageId) {
        return service.hasUserCompletedPackage(userId, packageId);
    }
    
    // Insurance module to get Booking ID
    @GetMapping("/internal/{id}")
    public ResponseEntity<Booking> getInternalBookingById(@PathVariable Long id) {
        Booking booking = service.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(booking);
    }
}
