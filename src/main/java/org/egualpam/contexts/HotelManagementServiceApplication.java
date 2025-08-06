package org.egualpam.contexts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
      "org.egualpam.contexts.hotel.shared.infrastructure.configuration",
      // Hotel -> Management
      "org.egualpam.contexts.hotel.management.hotel.infrastructure.configuration",
      "org.egualpam.contexts.hotel.management.hotel.infrastructure.controller",
      // Hotel -> Customer
      "org.egualpam.contexts.hotel.customer.hotel.infrastructure.configuration",
      "org.egualpam.contexts.hotel.customer.hotel.infrastructure.controller",
      // Hotel Rating
      "org.egualpam.contexts.hotel.management.hotelrating.infrastructure.configuration",
      // Review
      "org.egualpam.contexts.hotelmanagement.review.infrastructure.configuration",
      "org.egualpam.contexts.hotelmanagement.review.infrastructure.controller",
      // Room
      "org.egualpam.contexts.hotelmanagement.room.infrastructure.controller",
      "org.egualpam.contexts.hotelmanagement.room.infrastructure.configuration",
      // Reservation
      "org.egualpam.contexts.hotelmanagement.reservation.infrastructure.controller",
      "org.egualpam.contexts.hotelmanagement.reservation.infrastructure.configuration",
      // Room price
      "org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.controller",
      "org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.configuration"
    })
public class HotelManagementServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(HotelManagementServiceApplication.class, args);
  }
}
