package org.egualpam.contexts.hotel.customer.review.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.customer.review.application.command.CreateReview;
import org.egualpam.contexts.hotel.customer.review.application.command.CreateReviewCommand;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class SyncCreateReviewCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final CreateReview createReview;

  private static CreateReviewCommand toApplicationCommand(SyncCreateReviewCommand cmd) {
    return new CreateReviewCommand(
        cmd.reviewIdentifier(), cmd.hotelIdentifier(), cmd.rating(), cmd.comment());
  }

  @Override
  public void handle(Command command) {
    transactionTemplate.executeWithoutResult(
        transactionStatus ->
            Optional.of(command)
                .filter(SyncCreateReviewCommand.class::isInstance)
                .map(SyncCreateReviewCommand.class::cast)
                .map(SyncCreateReviewCommandHandler::toApplicationCommand)
                .ifPresent(createReview::execute));
  }
}
