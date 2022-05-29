package com.sbaitman.booking.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class MovieBooking extends Booking {
    private String movie;
    private String location;
    private String screen;
    private int seatNumber;

    public MovieBooking(String id, String movie, String location, String screen, Date date, String bookingName, String bookingEmail, int seatNumber) {
        super(id, date, bookingName, bookingEmail);
        this.movie = movie;
        this.location = location;
        this.screen = screen;
        this.seatNumber = seatNumber;
    }
}
