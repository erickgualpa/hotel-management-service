package org.egualpam.contexts.hotelmanagement.review.infrastructure.configuration;

import java.time.Clock;
import java.util.function.Supplier;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReview;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviews;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewApplicationConfiguration {

  @Bean
  public CreateReview createReview(
      Clock clock,
      Supplier<UniqueId> uniqueIdSupplier,
      AggregateRepository<Review> reviewRepository,
      EventBus eventBus) {
    return new CreateReview(clock, uniqueIdSupplier, reviewRepository, eventBus);
  }

  @Bean
  public UpdateReview updateReview(
      Clock clock,
      Supplier<UniqueId> uniqueIdSupplier,
      AggregateRepository<Review> reviewRepository,
      EventBus eventBus) {
    return new UpdateReview(clock, uniqueIdSupplier, reviewRepository, eventBus);
  }

  @Bean
  public FindReviews findReviews(
      ReadModelSupplier<ReviewCriteria, ManyReviews> manyReviewsReadModelSupplier) {
    return new FindReviews(manyReviewsReadModelSupplier);
  }
}
