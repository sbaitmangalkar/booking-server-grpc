package com.sbaitman.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class Booking {
    private String id;
    private Date date;
    private String bookingName;
    private String bookingEmail;
}
