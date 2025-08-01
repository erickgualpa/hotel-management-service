package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.reservation.application.command.CreateReservation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationApplicationConfiguration {

  @Bean
  public CreateReservation createReservation() {
    return new CreateReservation();
  }
}
