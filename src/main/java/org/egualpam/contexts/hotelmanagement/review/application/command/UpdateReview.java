package org.egualpam.contexts.hotelmanagement.review.application.command;

import java.time.Clock;
import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.review.domain.ReviewNotFound;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;

public class UpdateReview {

  private final Clock clock;

  private final AggregateRepository<Review> reviewRepository;
  private final EventBus eventBus;

  public UpdateReview(
      Clock clock, AggregateRepository<Review> reviewRepository, EventBus eventBus) {
    this.clock = clock;
    this.reviewRepository = reviewRepository;
    this.eventBus = eventBus;
  }

  public void execute(UpdateReviewCommand command) {
    Review review =
        Optional.of(command)
            .map(UpdateReviewCommand::reviewIdentifier)
            .map(AggregateId::new)
            .flatMap(reviewRepository::find)
            .orElseThrow(ReviewNotFound::new);

    review.updateComment(command.comment(), clock);
    reviewRepository.save(review);
    eventBus.publish(review.pullDomainEvents());
  }
}
