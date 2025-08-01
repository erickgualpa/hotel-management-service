package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.configuration;

import org.egualpam.contexts.hotelmanagement.reservation.application.command.CreateReservation;
import org.egualpam.contexts.hotelmanagement.reservation.domain.Reservation;
import org.egualpam.contexts.hotelmanagement.reservation.infrastructure.cqrs.command.simple.SyncCreateReservationCommand;
import org.egualpam.contexts.hotelmanagement.reservation.infrastructure.cqrs.command.simple.SyncCreateReservationCommandHandler;
import org.egualpam.contexts.hotelmanagement.reservation.infrastructure.repository.JdbcReservationRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class ReservationInfrastructureConfiguration {

  @Bean
  public AggregateRepository<Reservation> repositoryRepository(JdbcClient jdbcClient) {
    return new JdbcReservationRepository(jdbcClient);
  }

  @Bean
  public SimpleCommandBusConfiguration reservationSimpleCommandBusConfiguration(
      TransactionTemplate transactionTemplate, CreateReservation createReservation) {
    return new SimpleCommandBusConfiguration()
        .handling(
            SyncCreateReservationCommand.class,
            new SyncCreateReservationCommandHandler(transactionTemplate, createReservation));
  }
}
