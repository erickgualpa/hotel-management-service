package org.egualpam.services.hotelmanagement.hotels.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotels.application.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.application.SingleHotelView;
import org.egualpam.services.hotelmanagement.hotels.domain.Hotel;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaMultipleHotelsViewSupplier;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaSingleHotelViewSupplier;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelsConfiguration {

    @Bean
    public AggregateRepository<Hotel> hotelRepository(EntityManager entityManager) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }

    @Bean
    public ViewSupplier<SingleHotelView> hotelViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaSingleHotelViewSupplier(entityManager);
    }

    @Bean
    public ViewSupplier<MultipleHotelsView> hotelsViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaMultipleHotelsViewSupplier(entityManager);
    }
}
