package org.egualpam.contexts.hotelmanagement.review.infrastructure.configuration;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReview;
import org.egualpam.contexts.hotelmanagement.review.application.query.FindReviews;
import org.egualpam.contexts.hotelmanagement.review.application.query.ManyReviews;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewCriteria;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEventBus;
import org.egualpam.contexts.hotelmanagement.shared.application.query.ReadModelSupplier;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewApplicationConfiguration {

  @Bean
  public CreateReview createReview(
      Clock clock,
      AggregateRepository<Review> reviewRepository,
      InternalEventBus internalEventBus,
      EventBus eventBus) {
    return new CreateReview(clock, reviewRepository, internalEventBus, eventBus);
  }

  @Bean
  public UpdateReview updateReview(
      Clock clock, AggregateRepository<Review> reviewRepository, EventBus eventBus) {
    return new UpdateReview(clock, reviewRepository, eventBus);
  }

  @Bean
  public FindReviews findReviews(
      ReadModelSupplier<ReviewCriteria, ManyReviews> manyReviewsReadModelSupplier) {
    return new FindReviews(manyReviewsReadModelSupplier);
  }
}
