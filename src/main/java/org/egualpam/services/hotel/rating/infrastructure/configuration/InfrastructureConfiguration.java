package org.egualpam.services.hotel.rating.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.PostgreSqlJpaReviewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    public HotelRepository hotelRepository(EntityManager entityManager) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }

    @Bean
    public ReviewRepository reviewRepository(EntityManager entityManager) {
        return new PostgreSqlJpaReviewRepository(entityManager);
    }
}
