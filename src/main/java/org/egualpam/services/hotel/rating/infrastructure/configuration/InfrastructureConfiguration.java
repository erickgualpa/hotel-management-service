package org.egualpam.services.hotel.rating.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.persistence.EntityManager;
import org.egualpam.services.hotel.rating.application.shared.CommandBus;
import org.egualpam.services.hotel.rating.application.shared.QueryBus;
import org.egualpam.services.hotel.rating.domain.hotels.Hotel;
import org.egualpam.services.hotel.rating.domain.reviews.Review;
import org.egualpam.services.hotel.rating.domain.shared.AggregateRepository;
import org.egualpam.services.hotel.rating.domain.shared.DomainEventsPublisher;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.simple.SimpleCommandBus;
import org.egualpam.services.hotel.rating.infrastructure.cqrs.simple.SimpleQueryBus;
import org.egualpam.services.hotel.rating.infrastructure.events.publishers.simple.SimpleDomainEventsPublisher;
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
    public AggregateRepository<Hotel> aggregateHotelRepository(EntityManager entityManager) {
        return new PostgreSqlJpaHotelRepository(entityManager);
    }

    @Bean
    public AggregateRepository<Review> aggregateReviewRepository(EntityManager entityManager) {
        return new PostgreSqlJpaReviewRepository(entityManager);
    }

    @Bean
    public DomainEventsPublisher domainEventsPublisher(EntityManager entityManager) {
        return new SimpleDomainEventsPublisher(entityManager);
    }

    @Bean
    public CommandBus commandBus(
            AggregateRepository<Review> aggregateReviewRepository,
            DomainEventsPublisher domainEventsPublisher
    ) {
        return new SimpleCommandBus(
                aggregateReviewRepository,
                domainEventsPublisher
        );
    }

    @Bean
    public QueryBus queryBus(
            ObjectMapper objectMapper,
            AggregateRepository<Hotel> aggregateHotelRepository,
            AggregateRepository<Review> aggregateReviewRepository
    ) {
        return new SimpleQueryBus(
                objectMapper,
                aggregateHotelRepository,
                aggregateReviewRepository
        );
    }
}
