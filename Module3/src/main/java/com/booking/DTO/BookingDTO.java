package com.booking.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingDTO {
    private Long bookingId;
    private Long userId;
    private Long packageId;
    private LocalDate tripStartDate;
    private LocalDate tripEndDate;
    private String status;
    private Long paymentId;
}