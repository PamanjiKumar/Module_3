package com.booking.service;

import com.booking.client.TravelPackageClient;
import com.booking.dto.TravelPackageDTO;
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

    @Autowired
    private TravelPackageClient travelPackageClient;

    public Payment processPayment(Payment payment) {
        // 1. Validate Booking
        Long bookingId = payment.getBookingId();
        Booking booking = bookingRepo.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new IllegalArgumentException("Booking with ID " + bookingId + " not found.");
        }

        // 2. Validate Travel Package
        TravelPackageDTO pkg = travelPackageClient.getPackageById(booking.getPackageId());
        if (pkg == null) {
            throw new IllegalArgumentException("Invalid package ID associated with booking.");
        }

        // 3. Validate Payment Method and Details
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

        if (payment.getExpiryDate() == null || !payment.getExpiryDate().matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Expiry date must be in MM/YY format.");
        }

        String[] parts = payment.getExpiryDate().split("/");
        int expMonth = Integer.parseInt(parts[0]);
        int expYear = 2000 + Integer.parseInt(parts[1]);

        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();

        if (expMonth < 1 || expMonth > 12 || expYear < currentYear ||
           (expYear == currentYear && expMonth < currentMonth)) {
            throw new IllegalArgumentException("Card has expired.");
        }

        // 4. Amount Validation
        if (payment.getAmount() != pkg.getPrice()) {
            throw new IllegalArgumentException("Payment amount does not match the package price.");
        }

        // 5. Save Payment and Update Booking
        payment.setStatus("PAID");
        Payment savedPayment = paymentRepo.save(payment);

        booking.setPaymentId(savedPayment.getPaymentId());
        bookingRepo.save(booking);

        // 6. Send Notifications
        notificationService.notifyCustomer(booking, savedPayment);
        notificationService.notifyTravelAgent(booking, savedPayment);

        return savedPayment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepo.findById(id).orElse(null);
    }
}