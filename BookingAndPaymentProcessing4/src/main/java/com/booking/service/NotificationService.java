package com.booking.service;

import com.booking.entity.Booking;
import com.booking.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;
    private final String senderEmail = "indhaanman@gmail.com";
    private final String customerEmail = "snehithkumar535@gmail.com";
    private final String agentEmail = "padmajareddy630@gmail.com";

    //Booking details are sent to customers through Email
    public void notifyCustomer(Booking booking, Payment payment) {
        String subject = "Your Travel Booking is Confirmed – Booking ID: " + booking.getBookingId();
        String body = "Dear Customer,\n\n"
                + "Thank you for booking your travel with us!\n\n"
                + "Your booking has been successfully confirmed. Here are the details:\n\n"
                + "- Booking ID: " + booking.getBookingId() + "\n"
                + "- Package ID: " + booking.getPackageId() + "\n"
                + "- Travel Dates: " + booking.getStartDate() + " to " + booking.getEndDate() + "\n"
                + "- Payment Amount: " + payment.getAmount() + " "+ payment.getCurrency() + "\n"
                + "- Payment Status: " + payment.getStatus() + "\n\n"
                + "We look forward to providing you with a wonderful travel experience.\n\n"
                + "Warm regards,\nTravel Booking Team";

        sendEmail(customerEmail, subject, body);
        System.out.println("Email is sent to " + customerEmail);
    }

    //Booking details are sent to travel agent through Email
    public void notifyTravelAgent(Booking booking, Payment payment) {
        String subject = "New Booking Received – Booking ID: " + booking.getBookingId();
        String body = "Dear Travel Agent,\n\n"
                + "A new booking has been successfully made. Please find the details below:\n\n"
                + "- Booking ID: " + booking.getBookingId() + "\n"
                + "- Customer ID: " + booking.getUserId() + "\n"
                + "- Package ID: " + booking.getPackageId() + "\n"
                + "- Travel Dates: " + booking.getStartDate() + " to " + booking.getEndDate() + "\n"
                + "- Payment Amount: " + payment.getAmount() + " " + payment.getCurrency() + "\n"
                + "- Payment Status: " + payment.getStatus() + "\n\n"
                + "Please ensure all arrangements are in place for the customer's travel.\n\n"
                + "Best regards,\nTravel Booking System";

        sendEmail(agentEmail, subject, body);
        System.out.println("Email is sent to " + agentEmail);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(senderEmail);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);
        mailSender.send(mail);
    }
}
