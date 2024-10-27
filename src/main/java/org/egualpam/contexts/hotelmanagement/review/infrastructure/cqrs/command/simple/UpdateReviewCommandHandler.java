package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;

@RequiredArgsConstructor
public class UpdateReviewCommandHandler implements CommandHandler {

  private final UpdateReview updateReview;

  @Override
  public void handle(Command command) {
    Optional.of(command)
        .filter(UpdateReviewCommand.class::isInstance)
        .map(UpdateReviewCommand.class::cast)
        .ifPresent(updateReview::execute);
  }
}
