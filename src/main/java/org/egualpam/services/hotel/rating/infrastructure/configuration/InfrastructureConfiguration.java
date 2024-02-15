package org.egualpam.services.hotel.rating.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.persistence.EntityManager;
import org.egualpam.services.hotel.rating.domain.hotels.HotelRepository;
import org.egualpam.services.hotel.rating.domain.reviews.ReviewRepository;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.CommandBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.CommandFactory;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.QueryFactory;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.SimpleCommandBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.SimpleQueryBus;
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
    public HotelRepository hotelRepository(EntityManager entityManager) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }

    @Bean
    public ReviewRepository reviewRepository(EntityManager entityManager) {
        return new PostgreSqlJpaReviewRepository(entityManager);
    }

    @Bean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

    @Bean
    public CommandFactory reviewCommandFactory(ReviewRepository reviewRepository) {
        return new CommandFactory(reviewRepository);
    }

    @Bean
    public QueryBus queryBus() {
        return new SimpleQueryBus();
    }

    @Bean
    public QueryFactory queryFactory(HotelRepository hotelRepository, ReviewRepository reviewRepository) {
        return new QueryFactory(hotelRepository, reviewRepository);
    }
}
