package org.egualpam.contexts.hotel.customer.hotel.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotel.customer.hotel.application.query.FindHotel;
import org.egualpam.contexts.hotel.customer.hotel.application.query.FindHotels;
import org.egualpam.contexts.hotel.customer.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotel.customer.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotel.customer.hotel.domain.HotelCriteria;
import org.egualpam.contexts.hotel.customer.hotel.domain.UniqueHotelCriteria;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.cqrs.query.simple.SyncFindHotelQuery;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.cqrs.query.simple.SyncFindHotelQueryHandler;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.cqrs.query.simple.SyncFindHotelsQuery;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.cqrs.query.simple.SyncFindHotelsQueryHandler;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.readmodelsupplier.jpa.JpaManyHotelsReadModelSupplier;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.readmodelsupplier.jpa.JpaOneHotelReadModelSupplier;
import org.egualpam.contexts.hotel.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration("hotelCustomerInfrastructureConfiguration")
public class HotelInfrastructureConfiguration {

  @Bean
  public WebClient imageServiceClient(@Value("${clients.image-service.host}") String host) {
    return WebClient.create(host);
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
}
