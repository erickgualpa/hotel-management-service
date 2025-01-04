package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.CreateHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.UpdateHotelRating;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotelmanagement.hotel.domain.ReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.consumer.ReviewCreatedEventConsumer;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.consumer.SpringAmqpReviewDomainEventsConsumer;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple.AsyncCreateHotelCommand;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple.AsyncCreateHotelCommandHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple.SyncCreateHotelCommand;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple.SyncCreateHotelCommandHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple.SyncUpdateHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple.SyncUpdateHotelRatingCommandHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.SyncFindHotelQuery;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.SyncFindHotelQueryHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.SyncFindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.SyncFindHotelsQueryHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa.JpaManyHotelsReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.readmodelsupplier.jpa.JpaOneHotelReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.repository.jpa.JpaHotelRepository;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.reviewisalreadyprocessed.JpaReviewIsAlreadyProcessed;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.internaleventbus.spring.ReviewCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EntityScan("org.egualpam.contexts.hotelmanagement.hotel.infrastructure.shared.jpa")
public class HotelInfrastructureConfiguration {

  @Bean
  public WebClient imageServiceClient(@Value("${clients.image-service.host}") String host) {
    return WebClient.create(host);
  }

  @Bean
  public AggregateRepository<Hotel> hotelRepository(EntityManager entityManager) {
    return new JpaHotelRepository(entityManager);
  }

  @Bean
  public ReadModelSupplier<UniqueHotelCriteria, OneHotel> oneHotelReadModelSupplier(
      EntityManager entityManager, WebClient imageServiceClient) {
    return new JpaOneHotelReadModelSupplier(entityManager, imageServiceClient);
  }

  @Bean
  public ReadModelSupplier<HotelCriteria, ManyHotels> manyHotelsReadModelSupplier(
      EntityManager entityManager) {
    return new JpaManyHotelsReadModelSupplier(entityManager);
  }

  @Bean
  public SimpleQueryBusConfiguration hotelsSimpleQueryBusConfiguration(
      FindHotel findHotel, FindHotels findHotels) {
    return new SimpleQueryBusConfiguration()
        .handling(SyncFindHotelQuery.class, new SyncFindHotelQueryHandler(findHotel))
        .handling(SyncFindHotelsQuery.class, new SyncFindHotelsQueryHandler(findHotels));
  }

  @Bean
  public SimpleCommandBusConfiguration hotelsSimpleCommandBusConfiguration(
      TransactionTemplate transactionTemplate,
      CreateHotel createHotel,
      UpdateHotelRating updateHotelRating) {
    return new SimpleCommandBusConfiguration()
        .handling(
            SyncCreateHotelCommand.class,
            new SyncCreateHotelCommandHandler(transactionTemplate, createHotel))
        .handling(
            AsyncCreateHotelCommand.class,
            new AsyncCreateHotelCommandHandler(transactionTemplate, createHotel))
        .handling(
            SyncUpdateHotelRatingCommand.class,
            new SyncUpdateHotelRatingCommandHandler(transactionTemplate, updateHotelRating));
  }

  @Bean
  public ApplicationListener<ReviewCreatedEvent> reviewCreatedEventConsumer(CommandBus commandBus) {
    return new ReviewCreatedEventConsumer(commandBus);
  }

  @Bean
  public ReviewIsAlreadyProcessed reviewIsAlreadyProcessed(EntityManager entityManager) {
    return new JpaReviewIsAlreadyProcessed(entityManager);
  }

  @Bean
  public SpringAmqpReviewDomainEventsConsumer reviewCreatedPublicEventConsumer(
      ObjectMapper objectMapper, CommandBus commandBus) {
    return new SpringAmqpReviewDomainEventsConsumer(objectMapper, commandBus);
  }
}
