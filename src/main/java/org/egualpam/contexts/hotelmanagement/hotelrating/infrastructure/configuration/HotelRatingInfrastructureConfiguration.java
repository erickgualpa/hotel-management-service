package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.InitializeHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.consumer.SyncInitializeHotelRatingConsumer;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncInitializeHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SyncInitializeHotelRatingCommandHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
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
}
