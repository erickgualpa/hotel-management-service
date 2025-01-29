package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.InitializeHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.consumer.SyncInitializeHotelRatingConsumer;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncInitializeHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncInitializeHotelRatingCommandHandler;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.repository.jpa.JpaHotelRatingRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EntityScan("org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.repository.jpa")
public class HotelRatingInfrastructureConfiguration {

  @Bean
  public SyncInitializeHotelRatingConsumer syncInitializeHotelRatingConsumer(
      ObjectMapper objectMapper, CommandBus commandBus) {
    return new SyncInitializeHotelRatingConsumer(objectMapper, commandBus);
  }

  @Bean
  public SimpleCommandBusConfiguration hotelRatingSimpleCommandBusConfiguration(
      TransactionTemplate transactionTemplate, InitializeHotelRating initializeHotelRating) {
    return new SimpleCommandBusConfiguration()
        .handling(
            SyncInitializeHotelRatingCommand.class,
            new SyncInitializeHotelRatingCommandHandler(
                transactionTemplate, initializeHotelRating));
  }

  @Bean
  public AggregateRepository<HotelRating> repository(EntityManager entityManager) {
    return new JpaHotelRatingRepository(entityManager);
  }
}
