package org.egualpam.services.hotelmanagement.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.application.hotels.HotelView;
import org.egualpam.services.hotelmanagement.application.hotels.HotelsView;
import org.egualpam.services.hotelmanagement.application.reviews.ReviewsView;
import org.egualpam.services.hotelmanagement.application.shared.CommandBus;
import org.egualpam.services.hotelmanagement.application.shared.QueryBus;
import org.egualpam.services.hotelmanagement.application.shared.ViewSupplier;
import org.egualpam.services.hotelmanagement.domain.hotels.Hotel;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.DomainEventsPublisher;
import org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple.SimpleCommandBus;
import org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple.SimpleQueryBus;
import org.egualpam.services.hotelmanagement.infrastructure.events.publishers.simple.SimpleDomainEventsPublisher;
import org.egualpam.services.hotelmanagement.infrastructure.persistence.jpa.PostgreSqlJpaHotelRepository;
import org.egualpam.services.hotelmanagement.infrastructure.persistence.jpa.PostgreSqlJpaHotelViewSupplier;
import org.egualpam.services.hotelmanagement.infrastructure.persistence.jpa.PostgreSqlJpaHotelsViewSupplier;
import org.egualpam.services.hotelmanagement.infrastructure.persistence.jpa.PostgreSqlJpaReviewRepository;
import org.egualpam.services.hotelmanagement.infrastructure.persistence.jpa.PostgreSqlJpaReviewsViewSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfiguration {

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
    public ViewSupplier<HotelView> hotelViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaHotelViewSupplier(entityManager);
    }

    @Bean
    public ViewSupplier<HotelsView> hotelsViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaHotelsViewSupplier(entityManager);
    }

    @Bean
    public ViewSupplier<ReviewsView> reviewsViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaReviewsViewSupplier(entityManager);
    }

    @Bean
    public QueryBus queryBus(
            ViewSupplier<HotelView> hotelViewSupplier,
            ViewSupplier<HotelsView> hotelsViewSupplier,
            ViewSupplier<ReviewsView> reviewsViewSupplier
    ) {
        return new SimpleQueryBus(
                hotelViewSupplier,
                hotelsViewSupplier,
                reviewsViewSupplier
        );
    }
}
