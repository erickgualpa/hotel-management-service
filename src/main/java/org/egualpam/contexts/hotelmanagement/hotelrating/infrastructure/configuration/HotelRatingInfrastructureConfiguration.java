package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.CommandBus;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.command.InitializeHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.command.UpdateHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.domain.HotelRatingIdGenerator;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.consumer.SyncInitializeHotelRatingConsumer;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.consumer.SyncUpdateHotelRatingConsumer;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncInitializeHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncInitializeHotelRatingCommandHandler;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncUpdateHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple.SyncUpdateHotelRatingCommandHandler;
import org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.repository.jpa.JpaHotelRatingRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EntityScan("org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.repository.jpa")
public class HotelRatingInfrastructureConfiguration {

  @Bean
  public SyncInitializeHotelRatingConsumer syncInitializeHotelRatingConsumer(
      ObjectMapper objectMapper, CommandBus commandBus) {
    return new SyncInitializeHotelRatingConsumer(objectMapper, commandBus);
  }

  @Bean("syncUpdateHotelRatingConsumerV2")
  public SyncUpdateHotelRatingConsumer syncUpdateHotelRatingConsumer(
      ObjectMapper objectMapper, CommandBus commandBus) {
    return new SyncUpdateHotelRatingConsumer(objectMapper, commandBus);
  }

  @Bean
  public SimpleCommandBusConfiguration hotelRatingSimpleCommandBusConfiguration(
      TransactionTemplate transactionTemplate,
      InitializeHotelRating initializeHotelRating,
      UpdateHotelRating updateHotelRating) {
    return new SimpleCommandBusConfiguration()
        .handling(
            SyncInitializeHotelRatingCommand.class,
            new SyncInitializeHotelRatingCommandHandler(transactionTemplate, initializeHotelRating))
        .handling(
            SyncUpdateHotelRatingCommand.class,
            new SyncUpdateHotelRatingCommandHandler(transactionTemplate, updateHotelRating));
  }

  @Bean
  public AggregateRepository<HotelRating> repository(
      ObjectMapper objectMapper,
      EntityManager entityManager,
      NamedParameterJdbcTemplate jdbcTemplate) {
    return new JpaHotelRatingRepository(objectMapper, entityManager, jdbcTemplate);
  }

  @Bean
  public HotelRatingIdGenerator hotelRatingIdGenerator() {
    return (hotelId -> {
      var hotelIdBytes = hotelId.value().getBytes(StandardCharsets.UTF_8);
      var hotelRatingId = UUID.nameUUIDFromBytes(hotelIdBytes);
      return new AggregateId(hotelRatingId.toString());
    });
  }
}
