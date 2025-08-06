package org.egualpam.contexts.hotelmanagement.hotelrating.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.command.UpdateHotelRating;
import org.egualpam.contexts.hotelmanagement.hotelrating.application.command.UpdateHotelRatingCommand;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class SyncUpdateHotelRatingCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final UpdateHotelRating updateHotelRating;

  private static UpdateHotelRatingCommand toApplicationCommand(
      SyncUpdateHotelRatingCommand command) {
    return new UpdateHotelRatingCommand(
        command.hotelId(), command.reviewId(), command.reviewRating());
  }

  @Override
  public void handle(Command command) {
    transactionTemplate.executeWithoutResult(
        ts ->
            Optional.of(command)
                .filter(SyncUpdateHotelRatingCommand.class::isInstance)
                .map(SyncUpdateHotelRatingCommand.class::cast)
                .map(SyncUpdateHotelRatingCommandHandler::toApplicationCommand)
                .ifPresent(updateHotelRating::execute));
  }
}
