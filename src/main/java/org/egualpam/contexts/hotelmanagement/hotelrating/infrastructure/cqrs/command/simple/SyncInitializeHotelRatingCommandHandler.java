package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.InitializeHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.InitializeHotelRatingCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class SyncInitializeHotelRatingCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final InitializeHotelRating initializeHotelRating;

  private static InitializeHotelRatingCommand toApplicationCommand(
      SyncInitializeHotelRatingCommand command) {
    return new InitializeHotelRatingCommand();
  }

  @Override
  public void handle(Command command) {
    transactionTemplate.executeWithoutResult(
        ts ->
            Optional.of(command)
                .filter(SyncInitializeHotelRatingCommand.class::isInstance)
                .map(SyncInitializeHotelRatingCommand.class::cast)
                .map(SyncInitializeHotelRatingCommandHandler::toApplicationCommand)
                .ifPresent(initializeHotelRating::execute));
  }
}
