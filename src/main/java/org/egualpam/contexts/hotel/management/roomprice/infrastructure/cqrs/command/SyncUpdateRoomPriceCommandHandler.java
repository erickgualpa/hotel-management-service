package org.egualpam.contexts.hotel.management.roomprice.infrastructure.cqrs.command;

import org.egualpam.contexts.hotel.management.roomprice.application.command.UpdateRoomPrice;
import org.egualpam.contexts.hotel.management.roomprice.application.command.UpdateRoomPriceCommand;
import org.egualpam.contexts.hotel.management.roomprice.infrastructure.cqrs.command.simple.SyncUpdateRoomPriceCommand;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.springframework.transaction.support.TransactionTemplate;

public class SyncUpdateRoomPriceCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final UpdateRoomPrice updateRoomPrice;

  public SyncUpdateRoomPriceCommandHandler(
      TransactionTemplate transactionTemplate, UpdateRoomPrice updateRoomPrice) {
    this.transactionTemplate = transactionTemplate;
    this.updateRoomPrice = updateRoomPrice;
  }

  @Override
  public void handle(Command command) {
    final var syncUpdateRoomPriceCommand = (SyncUpdateRoomPriceCommand) command;

    final var applicationCommand =
        new UpdateRoomPriceCommand(
            syncUpdateRoomPriceCommand.hotelId(),
            syncUpdateRoomPriceCommand.roomType(),
            syncUpdateRoomPriceCommand.priceAmount());

    transactionTemplate.executeWithoutResult(
        ignored -> updateRoomPrice.execute(applicationCommand));
  }
}
