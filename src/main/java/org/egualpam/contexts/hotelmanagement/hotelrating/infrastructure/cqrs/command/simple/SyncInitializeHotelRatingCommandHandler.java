package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.command.InitializeHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.command.InitializeHotelRatingCommand;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class SyncInitializeHotelRatingCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final InitializeHotelRating initializeHotelRating;

  private static InitializeHotelRatingCommand toApplicationCommand(
      SyncInitializeHotelRatingCommand command) {
    return new InitializeHotelRatingCommand(command.hotelId());
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
