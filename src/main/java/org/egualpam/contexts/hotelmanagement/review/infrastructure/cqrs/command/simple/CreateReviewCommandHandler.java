package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.CreateReviewCommand;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;

@RequiredArgsConstructor
public class CreateReviewCommandHandler implements CommandHandler {

  private final CreateReview createReview;

  @Override
  public void handle(Command command) {
    Optional.of(command)
        .filter(CreateReviewCommand.class::isInstance)
        .map(CreateReviewCommand.class::cast)
        .ifPresent(createReview::execute);
  }
}
