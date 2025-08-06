package org.egualpam.contexts.hotel.customer.review.application.command;

import java.time.Clock;
import java.util.Optional;
import java.util.function.Supplier;
import org.egualpam.contexts.hotel.customer.review.domain.Review;
import org.egualpam.contexts.hotel.customer.review.domain.ReviewNotFound;
import org.egualpam.contexts.hotel.shared.domain.AggregateId;
import org.egualpam.contexts.hotel.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotel.shared.domain.EventBus;
import org.egualpam.contexts.hotel.shared.domain.UniqueId;

public class UpdateReview {

  private final Clock clock;
  private final Supplier<UniqueId> uniqueIdSupplier;
  private final AggregateRepository<Review> reviewRepository;
  private final EventBus eventBus;

  public UpdateReview(
      Clock clock,
      Supplier<UniqueId> uniqueIdSupplier,
      AggregateRepository<Review> reviewRepository,
      EventBus eventBus) {
    this.clock = clock;
    this.uniqueIdSupplier = uniqueIdSupplier;
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

    review.updateComment(command.comment(), clock, uniqueIdSupplier);
    reviewRepository.save(review);
    eventBus.publish(review.pullDomainEvents());
  }
}
