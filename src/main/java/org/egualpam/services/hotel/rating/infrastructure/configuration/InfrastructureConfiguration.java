package org.egualpam.services.hotel.rating.infrastructure.configuration;

import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistance.HotelQueryRepositoryImpl;
import org.egualpam.services.hotel.rating.infrastructure.persistance.PostgreSqlRatedHotelRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    public RatedHotelRepository ratedHotelRepository(EntityManager entityManager) {
        return new PostgreSqlRatedHotelRepository(entityManager);
    }

    @Bean
    public HotelQueryRepositoryImpl hotelQueryRepository(EntityManager entityManager) {
        return new HotelQueryRepositoryImpl(entityManager);
    }
}
