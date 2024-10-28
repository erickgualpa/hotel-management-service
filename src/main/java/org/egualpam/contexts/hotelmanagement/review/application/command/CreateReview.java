package org.egualpam.contexts.hotelmanagement.review.application.command;

import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalEventBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;

public final class CreateReview {

  private final AggregateRepository<Review> reviewRepository;
  private final InternalEventBus internalEventBus;
  private final EventBus eventBus;

  public CreateReview(
      AggregateRepository<Review> reviewRepository,
      InternalEventBus internalEventBus,
      EventBus eventBus) {
    this.reviewRepository = reviewRepository;
    this.internalEventBus = internalEventBus;
    this.eventBus = eventBus;
  }

  public void execute(CreateReviewCommand command) {
    String reviewId = command.reviewIdentifier();
    String hotelId = command.hotelIdentifier();
    Integer rating = command.rating();
    String comment = command.comment();

    Review review = Review.create(reviewRepository, reviewId, hotelId, rating, comment);

    reviewRepository.save(review);

    ReviewCreated internalEvent = new ReviewCreated(review.id(), review.hotelId(), review.rating());
    internalEventBus.publish(internalEvent);

    eventBus.publish(review.pullDomainEvents());
  }
}
