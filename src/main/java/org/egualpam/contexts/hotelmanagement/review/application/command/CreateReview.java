package org.egualpam.contexts.hotelmanagement.review.application.command;

import java.time.Clock;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.UniqueIdSupplier;

public final class CreateReview {

  private final Clock clock;
  private final UniqueIdSupplier uniqueIdSupplier;
  private final AggregateRepository<Review> reviewRepository;
  private final EventBus eventBus;

  public CreateReview(
      Clock clock,
      UniqueIdSupplier uniqueIdSupplier,
      AggregateRepository<Review> reviewRepository,
      EventBus eventBus) {
    this.clock = clock;
    this.uniqueIdSupplier = uniqueIdSupplier;
    this.reviewRepository = reviewRepository;
    this.eventBus = eventBus;
  }

  public void execute(CreateReviewCommand command) {
    String reviewId = command.reviewIdentifier();
    String hotelId = command.hotelIdentifier();
    Integer rating = command.rating();
    String comment = command.comment();

    Review review =
        Review.create(
            reviewRepository, reviewId, hotelId, rating, comment, clock, uniqueIdSupplier);

    reviewRepository.save(review);
    eventBus.publish(review.pullDomainEvents());
  }
}
