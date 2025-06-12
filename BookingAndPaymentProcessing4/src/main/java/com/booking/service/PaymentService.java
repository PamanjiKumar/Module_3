package com.booking.service;

import com.booking.entity.Booking;
import com.booking.entity.Payment;
import com.booking.repository.BookingRepository;
import com.booking.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private NotificationService notificationService;
    
    
    public Payment processPayment(Payment payment) {
        if (!payment.getPaymentMethod().equalsIgnoreCase("Credit Card") &&
            !payment.getPaymentMethod().equalsIgnoreCase("Debit Card")) {
            throw new IllegalArgumentException("Only Credit Card or Debit Card are accepted.");
        }

        if (payment.getCardNumber() == null || !payment.getCardNumber().matches("\\d{16}")) {
            throw new IllegalArgumentException("Invalid card number. Must be 16 digits.");
        }

        if (payment.getCvv() == null || !payment.getCvv().matches("\\d{3}")) {
            throw new IllegalArgumentException("Invalid CVV. Must be 3 digits.");
        }

        if (payment.getAtmPin() == null || !payment.getAtmPin().matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid ATM PIN. Must be 4 digits.");
        }
        
     // Expiry Date Format: MM/YY
        if (payment.getExpiryDate() == null || !payment.getExpiryDate().matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Expiry date must be in MM/YY format.");
        }
         
        String[] parts = payment.getExpiryDate().split("/");
        int expMonth = Integer.parseInt(parts[0]);
        int expYear = 2000 + Integer.parseInt(parts[1]); // e.g., "25" becomes 2025
         
        if (expMonth < 1 || expMonth > 12) {
            throw new IllegalArgumentException("Expiry month must be between 01 and 12.");
        }
         
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();
         
        if (expYear < currentYear || (expYear == currentYear && expMonth < currentMonth)) {
            throw new IllegalArgumentException("Card has expired.");
        }
         

        payment.setStatus("PAID");
        Payment savedPayment = paymentRepo.save(payment);

        // Fetch booking details using bookingId
        Long bookingId = Long.parseLong(payment.getBookingId());
        Booking booking = bookingRepo.findById(bookingId).orElse(null);

        if (booking != null) {
            notificationService.notifyCustomer(booking, savedPayment);
            notificationService.notifyTravelAgent(booking, savedPayment);
        }

        return savedPayment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepo.findById(id).orElse(null);
    }
   

}

