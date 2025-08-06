package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.cqrs.command.simple;

import java.util.Optional;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.egualpam.contexts.hotelmanagement.reservation.application.command.CreateReservation;
import org.egualpam.contexts.hotelmanagement.reservation.application.command.CreateReservationCommand;
import org.springframework.transaction.support.TransactionTemplate;

public class SyncCreateReservationCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final CreateReservation createReservation;

  public SyncCreateReservationCommandHandler(
      TransactionTemplate transactionTemplate, CreateReservation createReservation) {
    this.transactionTemplate = transactionTemplate;
    this.createReservation = createReservation;
  }

  public static CreateReservationCommand toApplicationCommand(
      SyncCreateReservationCommand command) {
    return new CreateReservationCommand(
        command.id(), command.roomId(), command.from(), command.to());
  }

  @Override
  public void handle(Command command) {
    CreateReservationCommand applicationCommand =
        Optional.of(command)
            .filter(SyncCreateReservationCommand.class::isInstance)
            .map(SyncCreateReservationCommand.class::cast)
            .map(SyncCreateReservationCommandHandler::toApplicationCommand)
            .orElseThrow();

    transactionTemplate.executeWithoutResult(
        ignored -> createReservation.execute(applicationCommand));
  }
}
