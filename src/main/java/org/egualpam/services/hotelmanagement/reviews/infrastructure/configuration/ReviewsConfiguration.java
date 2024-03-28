package org.egualpam.services.hotelmanagement.reviews.infrastructure.configuration;

import jakarta.persistence.EntityManager;
import org.egualpam.services.hotelmanagement.domain.reviews.Review;
import org.egualpam.services.hotelmanagement.reviews.application.ReviewsView;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa.PostgreSqlJpaReviewRepository;
import org.egualpam.services.hotelmanagement.reviews.infrastructure.persistence.jpa.PostgreSqlJpaReviewsViewSupplier;
import org.egualpam.services.hotelmanagement.shared.application.ViewSupplier;
import org.egualpam.services.hotelmanagement.shared.domain.AggregateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewsConfiguration {

    @Bean
    public AggregateRepository<Review> reviewRepository(EntityManager entityManager) {
        return new PostgreSqlJpaReviewRepository(entityManager);
    }

    @Bean
    public ViewSupplier<ReviewsView> reviewsViewSupplier(EntityManager entityManager) {
        return new PostgreSqlJpaReviewsViewSupplier(entityManager);
    }
}
