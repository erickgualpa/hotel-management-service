package org.egualpam.contexts.hotelmanagement.review.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReview;
import org.egualpam.contexts.hotelmanagement.review.application.command.UpdateReviewCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class UpdateReviewCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final UpdateReview updateReview;

  private static UpdateReviewCommand toApplicationCommand(SyncUpdateReviewCommand cmd) {
    return new UpdateReviewCommand(cmd.reviewIdentifier(), cmd.comment());
  }

  @Override
  public void handle(Command command) {
    transactionTemplate.executeWithoutResult(
        transactionStatus ->
            Optional.of(command)
                .filter(SyncUpdateReviewCommand.class::isInstance)
                .map(SyncUpdateReviewCommand.class::cast)
                .map(UpdateReviewCommandHandler::toApplicationCommand)
                .ifPresent(updateReview::execute));
  }
}
