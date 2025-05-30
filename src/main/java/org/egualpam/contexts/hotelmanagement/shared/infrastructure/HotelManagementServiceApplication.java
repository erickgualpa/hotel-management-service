package org.egualpam.contexts.hotelmanagement.shared.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {
      "org.egualpam.contexts.hotelmanagement.shared.infrastructure.configuration",
      "org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration",
      "org.egualpam.contexts.hotelmanagement.hotel.infrastructure.controller",
      "org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.configuration",
      "org.egualpam.contexts.hotelmanagement.review.infrastructure.configuration",
      "org.egualpam.contexts.hotelmanagement.review.infrastructure.controller",
    })
public class HotelManagementServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(HotelManagementServiceApplication.class, args);
  }
}
