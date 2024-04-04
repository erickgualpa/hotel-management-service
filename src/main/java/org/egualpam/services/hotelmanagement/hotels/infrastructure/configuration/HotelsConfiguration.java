package org.egualpam.services.hotelmanagement.hotels.infrastructure.configuration;

import jakarta.persistence.EntityManager;
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
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelsConfiguration {

    @Bean
    public AggregateRepository<Hotel> hotelRepository(EntityManager entityManager) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }

    @Bean
    public ViewSupplier<SingleHotelView> singleHotelViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaSingleHotelViewSupplier(entityManager);
    }

    @Bean
    public ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaMultipleHotelsViewSupplier(entityManager);
    }

    @Bean
    public QueryHandler findHotelQueryHandler(ViewSupplier<SingleHotelView> singleHotelViewSupplier) {
        return new FindHotelQueryHandler(singleHotelViewSupplier);
    }

    @Bean
    public QueryHandler findHotelsQueryHandler(ViewSupplier<MultipleHotelsView> multipleHotelsViewSupplier) {
        return new FindHotelsQueryHandler(multipleHotelsViewSupplier);
    }
}
