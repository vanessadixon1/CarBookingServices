package com.amcsoftware.carbookingservices.customResponse;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomReceiptResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private String message;
}
