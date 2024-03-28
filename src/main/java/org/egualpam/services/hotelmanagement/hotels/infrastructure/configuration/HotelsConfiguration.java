package org.egualpam.services.hotelmanagement.hotels.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.domain.hotels.Hotel;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.hotels.application.HotelView;
import org.egualpam.services.hotelmanagement.hotels.application.HotelsView;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaHotelViewSupplier;
import org.egualpam.services.hotelmanagement.hotels.infrastructure.persistence.jpa.PostgreSqlJpaHotelsViewSupplier;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelsConfiguration {

    @Bean
    public AggregateRepository<Hotel> hotelRepository(EntityManager entityManager) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }

    @Bean
    public ViewSupplier<HotelView> hotelViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaHotelViewSupplier(entityManager);
    }

    @Bean
    public ViewSupplier<HotelsView> hotelsViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaHotelsViewSupplier(entityManager);
    }
}
