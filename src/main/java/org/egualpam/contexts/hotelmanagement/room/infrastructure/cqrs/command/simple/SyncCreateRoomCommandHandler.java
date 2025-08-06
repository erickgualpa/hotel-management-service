package org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.command.simple;

import java.util.Optional;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.egualpam.contexts.hotelmanagement.room.application.command.CreateRoom;
import org.egualpam.contexts.hotelmanagement.room.application.command.CreateRoomCommand;
import org.springframework.transaction.support.TransactionTemplate;

public class SyncCreateRoomCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final CreateRoom createRoom;

  public SyncCreateRoomCommandHandler(
      TransactionTemplate transactionTemplate, CreateRoom createRoom) {
    this.transactionTemplate = transactionTemplate;
    this.createRoom = createRoom;
  }

  private static CreateRoomCommand toApplicationCommand(SyncCreateRoomCommand command) {
    return new CreateRoomCommand(command.id(), command.type(), command.hotelId());
  }

  @Override
  public void handle(Command command) {
    transactionTemplate.executeWithoutResult(
        ts -> {
          Optional.of(command)
              .filter(SyncCreateRoomCommand.class::isInstance)
              .map(SyncCreateRoomCommand.class::cast)
              .map(SyncCreateRoomCommandHandler::toApplicationCommand)
              .ifPresent(createRoom::execute);
        });
  }
}
