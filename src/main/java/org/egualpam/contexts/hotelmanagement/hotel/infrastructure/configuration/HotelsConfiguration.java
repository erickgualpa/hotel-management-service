package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.FindHotelQueryHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.FindHotelsQueryHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa.PostgreSqlJpaManyHotelsReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa.PostgreSqlJpaOneHotelReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HotelsConfiguration {

  @Bean
  public WebClient imageServiceClient(@Value("${clients.image-service.host}") String host) {
    return WebClient.create(host);
  }

  @Bean
  public AggregateRepository<Hotel> hotelRepository(EntityManager entityManager) {
    return new PostgreSqlJpaHotelRepository(entityManager);
  }

  @Bean
  public ReadModelSupplier<OneHotel> oneHotelReadModelSupplier(
      EntityManager entityManager, WebClient imageServiceClient) {
    return new PostgreSqlJpaOneHotelReadModelSupplier(entityManager, imageServiceClient);
  }

  @Bean
  public ReadModelSupplier<ManyHotels> manyHotelsReadModelSupplier(EntityManager entityManager) {
    return new PostgreSqlJpaManyHotelsReadModelSupplier(entityManager);
  }

  @Bean
  public SimpleQueryBusConfiguration hotelsSimpleQueryBusConfiguration(
      ReadModelSupplier<OneHotel> oneHotelReadModelSupplier,
      ReadModelSupplier<ManyHotels> manyHotelsReadModelSupplier) {
    FindHotels findHotels = new FindHotels(manyHotelsReadModelSupplier);
    return new SimpleQueryBusConfiguration()
        .withHandler(FindHotelQuery.class, new FindHotelQueryHandler(oneHotelReadModelSupplier))
        .withHandler(FindHotelsQuery.class, new FindHotelsQueryHandler(findHotels));
  }
}
