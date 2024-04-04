package org.egualpam.services.hotelmanagement.hotels.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotels.application.query.FindHotelQuery;
import org.egualpam.services.hotelmanagement.hotels.application.query.FindHotelsQuery;
import org.egualpam.services.hotelmanagement.hotels.application.query.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.application.query.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.Hotel;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.cqrs.query.simple.FindHotelQueryHandler;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.cqrs.query.simple.FindHotelsQueryHandler;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaMultipleHotelsViewSupplier;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaSingleHotelViewSupplier;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HotelsConfiguration {

    // TODO: Amend this bean to make it available also on application statup
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
