package org.egualpam.contexts.hotelmanagement.review.application.command;

import java.util.Optional;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.shared.application.command.InternalCommand;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateId;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;

public class UpdateReview implements InternalCommand {

  private final String reviewId;
  private final String comment;
  private final AggregateRepository<Review> reviewRepository;
  private final EventBus eventBus;

  public UpdateReview(
      String reviewId,
      String comment,
      AggregateRepository<Review> reviewRepository,
      EventBus eventBus) {
    this.reviewId = reviewId;
    this.comment = comment;
    this.reviewRepository = reviewRepository;
    this.eventBus = eventBus;
  }

  @Override
  public void execute() {
    Review review =
        Optional.of(reviewId).map(AggregateId::new).flatMap(reviewRepository::find).orElseThrow();
    review.updateComment(comment);
    reviewRepository.save(review);
    eventBus.publish(review.pullDomainEvents());
  }
}
