package org.egualpam.contexts.hotel.management.hotel.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotel.management.hotel.application.command.CreateHotel;
import org.egualpam.contexts.hotel.management.hotel.domain.Hotel;
import org.egualpam.contexts.hotel.management.hotel.infrastructure.cqrs.command.simple.AsyncCreateHotelCommand;
import org.egualpam.contexts.hotel.management.hotel.infrastructure.cqrs.command.simple.AsyncCreateHotelCommandHandler;
import org.egualpam.contexts.hotel.management.hotel.infrastructure.cqrs.command.simple.SyncCreateHotelCommand;
import org.egualpam.contexts.hotel.management.hotel.infrastructure.cqrs.command.simple.SyncCreateHotelCommandHandler;
import org.egualpam.contexts.hotel.management.hotel.infrastructure.repository.jpa.JpaHotelRepository;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration("hotelManagementInfrastructureConfiguration")
@EntityScan("org.egualpam.contexts.hotel.management.hotel.infrastructure.shared.jpa")
public class HotelInfrastructureConfiguration {

  @Bean
  public AggregateRepository<Hotel> hotelRepository(EntityManager entityManager) {
    return new JpaHotelRepository(entityManager);
  }

  @Bean
  public SimpleCommandBusConfiguration hotelsSimpleCommandBusConfiguration(
      TransactionTemplate transactionTemplate, CreateHotel createHotel) {
    return new SimpleCommandBusConfiguration()
        .handling(
            SyncCreateHotelCommand.class,
            new SyncCreateHotelCommandHandler(transactionTemplate, createHotel))
        .handling(
            AsyncCreateHotelCommand.class,
            new AsyncCreateHotelCommandHandler(transactionTemplate, createHotel));
  }
}
