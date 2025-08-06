package org.egualpam.contexts.hotel.management.reservation.infrastructure.configuration;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.management.reservation.application.command.CreateReservation;
import org.egualpam.contexts.hotel.management.reservation.domain.Reservation;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReservationApplicationConfiguration {

  @Bean
  public CreateReservation createReservation(
      Supplier<UniqueId> uniqueIdSupplier,
      AggregateRepository<Reservation> reservationRepository,
      EventBus eventBus,
      Clock clock) {
    return new CreateReservation(uniqueIdSupplier, reservationRepository, eventBus, clock);
  }
}
