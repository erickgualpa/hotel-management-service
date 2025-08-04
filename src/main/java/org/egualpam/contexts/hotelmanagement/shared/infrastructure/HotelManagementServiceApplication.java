package org.egualpam.contexts.hotelmanagement.shared.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
      "org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration",
      // Hotel
      "org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration",
      "org.egualpam.contexts.hotelmanagement.hotel.infrastructure.controller",
      // Hotel Rating
      "org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.configuration",
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
      "org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.controller"
    })
public class HotelManagementServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(HotelManagementServiceApplication.class, args);
  }
}
