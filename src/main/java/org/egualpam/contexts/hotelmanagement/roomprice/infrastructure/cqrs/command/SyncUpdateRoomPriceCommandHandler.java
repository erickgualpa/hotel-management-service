package org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.cqrs.command;

import org.egualpam.contexts.hotelmanagement.roomprice.application.command.UpdateRoomPrice;
import org.egualpam.contexts.hotelmanagement.roomprice.application.command.UpdateRoomPriceCommand;
import org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.cqrs.command.simple.SyncUpdateRoomPriceCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;
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
