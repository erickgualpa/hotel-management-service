package org.egualpam.services.hotelmanagement.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.shared.application.command.CommandBus;
import org.egualpam.services.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple.SimplePublicEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.stream.Collectors.toMap;

@Configuration
public class SharedConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("Hotel Management Service API")
                );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public PublicEventBus publicEventBus(EntityManager entityManager) {
        return new SimplePublicEventBus(entityManager);
    }

    @Bean
    public CommandBus commandBus(
            AggregateRepository<Review> reviewRepository,
            PublicEventBus publicEventBus
    ) {
        return new SimpleCommandBus(
                reviewRepository,
                publicEventBus
        );
    }

    @Bean
    public QueryBus queryBus(List<QueryHandler> queryHandlers) {
        return new SimpleQueryBus(
                queryHandlers.stream().collect(toMap(QueryHandler::type, h -> h))
        );
    }
}
