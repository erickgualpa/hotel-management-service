package org.egualpam.services.hotelmanagement.reviews.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.reviews.application.command.CreateReviewCommand;
import org.egualpam.services.hotelmanagement.reviews.application.command.UpdateReviewCommand;
import org.egualpam.services.hotelmanagement.reviews.application.query.FindReviewsQuery;
import org.egualpam.services.hotelmanagement.reviews.application.query.MultipleReviewsView;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.cqrs.command.simple.CreateReviewCommandHandler;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.cqrs.command.simple.UpdateReviewCommandHandler;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.cqrs.query.simple.FindReviewsQueryHandler;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa.PostgreSqlJpaMultipleReviewsViewSupplier;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa.PostgreSqlJpaReviewRepository;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.domain.PublicEventBus;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple.SimpleCommandBusConfiguration;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.SimpleQueryBusConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewsConfiguration {

    @Bean
    public AggregateRepository<Review> reviewRepository(
            EntityManager entityManager
    ) {
        return new PostgreSqlJpaReviewRepository(entityManager);
    }

    @Bean
    public ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier(
            EntityManager entityManager
    ) {
        return new PostgreSqlJpaMultipleReviewsViewSupplier(entityManager);
    }

    @Bean
    public SimpleCommandBusConfiguration reviewsSimpleCommandBusConfiguration(
            AggregateRepository<Review> reviewRepository,
            PublicEventBus publicEventBus
    ) {
        return new SimpleCommandBusConfiguration()
                .withHandler(
                        CreateReviewCommand.class,
                        new CreateReviewCommandHandler(reviewRepository, publicEventBus))
                .withHandler(
                        UpdateReviewCommand.class,
                        new UpdateReviewCommandHandler(reviewRepository, publicEventBus));
    }

    @Bean
    public SimpleQueryBusConfiguration reviewsSimpleQueryBusConfiguration(
            ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier
    ) {
        return new SimpleQueryBusConfiguration()
                .withHandler(FindReviewsQuery.class, new FindReviewsQueryHandler(multipleReviewsViewSupplier));
    }
}
