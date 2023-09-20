package org.egualpam.services.hotel.rating.infrastructure.configuration;

import org.egualpam.services.hotel.rating.domain.HotelRepository;
import org.egualpam.services.hotel.rating.domain.ReviewRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.jpa.PostgreSqlJpaReviewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

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
