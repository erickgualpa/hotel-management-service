package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.MultipleHotelsView;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.SingleHotelView;
import org.egualpam.contexts.hotelmanagement.hotel.domain.Hotel;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.FindHotelQueryHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.FindHotelsQueryHandler;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa.PostgreSqlJpaMultipleHotelsViewSupplier;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.persistence.jpa.PostgreSqlJpaSingleHotelViewSupplier;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ViewSupplier;
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
    public AggregateRepository<Hotel> hotelRepository(
            EntityManager entityManager
    ) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }

    @Bean
    public ViewSupplier<SingleHotelView> singleHotelViewSupplier(
            EntityManager entityManager,
            WebClient imageServiceClient
    ) {
        return new PostgreSqlJpaSingleHotelViewSupplier(entityManager, imageServiceClient);
    }

    @Bean
    public ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier(
            EntityManager entityManager
    ) {
        return new PostgreSqlJpaMultipleHotelsViewSupplier(entityManager);
    }

    @Bean
    public SimpleQueryBusConfiguration hotelsSimpleQueryBusConfiguration(
            ViewSupplier<SingleHotelView> singleHotelViewSupplier,
            ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier
    ) {
        return new SimpleQueryBusConfiguration()
                .withHandler(FindHotelQuery.class, new FindHotelQueryHandler(singleHotelViewSupplier))
                .withHandler(FindHotelsQuery.class, new FindHotelsQueryHandler(multipleHotelsViewSupplier));
    }
}
