package com.booking.controller;

import com.booking.DTO.BookingDTO;
import com.booking.entity.Booking;

import com.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

//    @PostMapping
//    public Booking create(@RequestBody Booking booking) {
//        return bookingService.createBooking(booking);
//    }
//    
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody Booking booking) {
        BookingDTO bookingDTO = bookingService.createBooking(booking);
        return new ResponseEntity<>(bookingDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Booking> getAll() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

   @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }
    


}
