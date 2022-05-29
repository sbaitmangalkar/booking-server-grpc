package com.sbaitman.booking.grpc.streaming;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BookingServer {
    private final int port;
    private final Server server;
    private static Logger logger = Logger.getLogger(BookingServer.class.getName());

    public BookingServer(int port) {
        this.port = port;
        this.server = ServerBuilder.forPort(port).addService(new BookingService()).build();
    }

    public void start() throws IOException {
        server.start();
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    System.err.println("shutting down server");
                    try {
                        BookingServer.this.stop();
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                    System.err.println("server shutted down");
                }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown()
                    .awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws Exception {
        logger.info("Starting booking server at port 9090");
        BookingServer bookingServer = new BookingServer(9090);
        bookingServer.start();
        logger.info( "Booking server started");
        if (bookingServer.server != null) {
            bookingServer.server.awaitTermination();
        }
    }
}
