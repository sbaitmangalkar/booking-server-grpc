package com.sbaitman.booking.grpc.streaming;

import com.sbaitman.booking.model.Booking;
import com.sbaitman.booking.model.MovieBooking;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class BookingService extends MovieBookingServiceGrpc.MovieBookingServiceImplBase {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");
    private static final Logger logger = Logger.getLogger(BookingService.class.getName());
    private List<Booking> bookings;

    public BookingService() {
        bookings = new ArrayList<>();
    }

    @Override
    public void getBookingDetails(BookingQuery request, StreamObserver<BookingDetails> responseObserver) {
        BookingDetails.Builder builder = BookingDetails.newBuilder();
        if (bookings != null && bookings.size() > 0 && request.getId() != null) {
            logger.info("Fetching booking details for id: " + request.getId());
            Optional<Booking> bookingOptional = bookings.stream().filter(booking -> request.getId().equalsIgnoreCase(booking.getId())).findFirst();
            if (bookingOptional.isPresent()) {
                var booking = bookingOptional.get();
                if(booking instanceof MovieBooking) {
                    MovieBooking movieBooking = (MovieBooking) booking;
                    Instant instant = movieBooking.getDate().toInstant();
                    BookingDetails bookingDetails = builder.setStatus("SUCCESS")
                            .setId(movieBooking.getId())
                            .setBookingName(movieBooking.getBookingName())
                            .setBookingEmail(movieBooking.getBookingEmail())
                            .setMovie(movieBooking.getMovie())
                            .setSeatNumber(movieBooking.getSeatNumber())
                            .setScreen(movieBooking.getScreen())
                            .setLocation(movieBooking.getLocation())
                            .setDate(instant.toString()).build();
                    responseObserver.onNext(bookingDetails);
                }
            }
        }
        responseObserver.onNext(null);
        responseObserver.onCompleted();
    }

    @Override
    public void makeBooking(BookingQuery request, StreamObserver<BookingDetails> responseObserver) {
        logger.info("Creating booking for request: " + request);
        String status = "FAILED";
        Instant now = Instant.now();
        int seatNumber = 1;
        LocalDateTime localDateTime = now.atZone(ZoneId.of("UTC")).toLocalDateTime();
        BookingDetails.Builder builder = BookingDetails.newBuilder();
        if (request != null) {
            MovieBooking booking = new MovieBooking(UUID.randomUUID().toString(), request.getMovie(), request.getLocation(), "S1", Date.from(now), request.getBookingName(), request.getBookingEmail(), seatNumber++);
            bookings.add(booking);
            status = "SUCCESS";
            builder.setId(booking.getId())
                    .setBookingName(request.getBookingName())
                    .setBookingEmail(request.getBookingEmail())
                    .setDate(localDateTime.format(formatter))
                    .setLocation(request.getLocation())
                    .setMovie(request.getMovie())
                    .setScreen(booking.getScreen())
                    .setSeatNumber(booking.getSeatNumber())
                    .setStatus(status);
        }
        BookingDetails bookingDetails = builder.setStatus(status).build();
        logger.info("Successfully created booking with id: " + bookingDetails.getId());
        responseObserver.onNext(bookingDetails);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllBookingsByLocation(BookingQuery request, StreamObserver<BookingDetails> responseObserver) {
        super.getAllBookingsByLocation(request, responseObserver);
    }

    @Override
    public void getAllBookingsByName(BookingQuery request, StreamObserver<BookingDetails> responseObserver) {
        super.getAllBookingsByName(request, responseObserver);
    }

    @Override
    public void getAllBookingsByMovie(BookingQuery request, StreamObserver<BookingDetails> responseObserver) {
        super.getAllBookingsByMovie(request, responseObserver);
    }
}
