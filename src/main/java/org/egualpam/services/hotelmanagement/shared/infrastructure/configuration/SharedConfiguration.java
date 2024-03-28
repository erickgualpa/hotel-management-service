package org.egualpam.services.hotelmanagement.shared.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.hotels.application.MultipleHotelsView;
import org.egualpam.services.hotelmanagement.hotels.application.SingleHotelView;
import org.egualpam.services.hotelmanagement.reviews.application.ReviewsView;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.shared.application.CommandBus;
import org.egualpam.services.hotelmanagement.shared.application.QueryBus;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.eventbus.simple.simple.SimplePublicEventBus;
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
    public QueryBus queryBus(
            ViewSupplier<SingleHotelView> hotelViewSupplier,
            ViewSupplier<MultipleHotelsView> hotelsViewSupplier,
            ViewSupplier<ReviewsView> reviewsViewSupplier
    ) {
        return new SimpleQueryBus(
                hotelViewSupplier,
                hotelsViewSupplier,
                reviewsViewSupplier
        );
    }
}
