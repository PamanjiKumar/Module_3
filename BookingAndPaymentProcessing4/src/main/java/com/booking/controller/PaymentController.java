package com.booking.controller;

import com.booking.DTO.PaymentResponseDTO;
import com.booking.entity.Payment;
import com.booking.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public PaymentResponseDTO process(@RequestBody Payment payment) {
        // Save the payment using the service
        Payment savedPayment = paymentService.processPayment(payment);

        // Build the response DTO from the saved entity
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setPaymentId(savedPayment.getPaymentId());
        response.setUserId(savedPayment.getUserId());
        response.setBookingId(savedPayment.getBookingId());
        response.setAmount(savedPayment.getAmount());
        response.setStatus(savedPayment.getStatus());
        response.setPaymentMethod(savedPayment.getPaymentMethod());
        response.setCurrency(savedPayment.getCurrency());
        return response;
    }

    @GetMapping
    public List<Payment> getAll() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }
}
