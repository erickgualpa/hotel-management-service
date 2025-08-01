package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.reservation.application.command.CreateReservation;
import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationApplicationConfiguration {

  @Bean
  public CreateReservation createReservation(
      AggregateRepository<Reservation> reservationRepository) {
    return new CreateReservation(reservationRepository);
  }
}
