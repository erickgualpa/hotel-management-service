package org.egualpam.services.hotelmanagement.reviews.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.reviews.application.query.MultipleReviewsView;
import org.egualpam.services.hotelmanagement.reviews.domain.Review;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.cqrs.query.simple.FindReviewsQueryHandler;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa.PostgreSqlJpaMultipleReviewsViewSupplier;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa.PostgreSqlJpaReviewRepository;
import org.egualpam.services.hotelmanagement.shared.application.query.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple.QueryHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewsConfiguration {

    @Bean
    public AggregateRepository<Review> reviewRepository(EntityManager entityManager) {
        return new PostgreSqlJpaReviewRepository(entityManager);
    }

    @Bean
    public ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaMultipleReviewsViewSupplier(entityManager);
    }

    @Bean
    public QueryHandler findReviewsQueryHandler(ViewSupplier<MultipleReviewsView> multipleReviewsViewSupplier) {
        return new FindReviewsQueryHandler(multipleReviewsViewSupplier);
    }
}
