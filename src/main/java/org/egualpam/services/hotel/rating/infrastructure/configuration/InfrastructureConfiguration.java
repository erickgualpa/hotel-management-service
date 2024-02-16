package org.egualpam.services.hotel.rating.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.persistence.EntityManager;
import org.egualpam.services.hotel.rating.application.shared.CommandBus;
import org.egualpam.services.hotel.rating.application.shared.QueryBus;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.simple.SimpleCommandBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.simple.SimpleQueryBus;
import org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.services.hotel.rating.infrastructure.persistence.jpa.PostgreSqlJpaReviewRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Hotel Rating Service API")
                );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public HotelRepository hotelRepository(EntityManager entityManager) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }

    @Bean
    public ReviewRepository reviewRepository(EntityManager entityManager) {
        return new PostgreSqlJpaReviewRepository(entityManager);
    }

    @Bean
    public CommandBus commandBus(ReviewRepository reviewRepository) {
        return new SimpleCommandBus(reviewRepository);
    }

    @Bean
    public QueryBus queryBus(
            ObjectMapper objectMapper,
            HotelRepository hotelRepository,
            ReviewRepository reviewRepository) {
        return new SimpleQueryBus(objectMapper, hotelRepository, reviewRepository);
    }
}
