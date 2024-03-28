package org.egualpam.services.hotelmanagement.shared.infrastructure.configuration;

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
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.domain.shared.AggregateRepository;
import org.egualpam.services.hotelmanagement.domain.shared.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.cqrs.command.simple.SimpleCommandBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.cqrs.query.simple.SimpleQueryBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple.simple.SimplePublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PostgreSqlJpaReviewRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.persistence.jpa.PostgreSqlJpaReviewsViewSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public AggregateRepository<Review> reviewRepository(EntityManager entityManager) {
        return new PostgreSqlJpaReviewRepository(entityManager);
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
