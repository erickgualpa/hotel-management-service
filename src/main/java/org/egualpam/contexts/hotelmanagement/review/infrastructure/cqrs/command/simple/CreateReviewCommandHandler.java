package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReviewCommand;
import org.egualpam.contexts.hotelmanagement.review.domain.Review;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.domain.AggregateRepository;
import org.egualpam.contexts.hotelmanagement.shared.domain.EventBus;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;

@RequiredArgsConstructor
public class CreateReviewCommandHandler implements CommandHandler {

  private final AggregateRepository<Review> reviewRepository;
  private final EventBus eventBus;

  @Override
  public void handle(Command command) {
    Optional.of(command)
        .filter(CreateReviewCommand.class::isInstance)
        .map(CreateReviewCommand.class::cast)
        .map(
            cmd ->
                new CreateReview(
                    cmd.reviewIdentifier(),
                    cmd.hotelIdentifier(),
                    cmd.rating(),
                    cmd.comment(),
                    reviewRepository,
                    eventBus))
        .ifPresent(CreateReview::execute);
  }
}
