package org.egualpam.contexts.hotel.management.hotel.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.management.hotel.application.command.CreateHotel;
import org.egualpam.contexts.hotel.management.hotel.application.command.CreateHotelCommand;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class SyncCreateHotelCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final CreateHotel createHotel;

  private static CreateHotelCommand toApplicationCommand(SyncCreateHotelCommand cmd) {
    return new CreateHotelCommand(
        cmd.id(), cmd.name(), cmd.description(), cmd.location(), cmd.price(), cmd.imageURL());
  }

  @Override
  public void handle(Command command) {
    transactionTemplate.executeWithoutResult(
        ts ->
            Optional.of(command)
                .filter(SyncCreateHotelCommand.class::isInstance)
                .map(SyncCreateHotelCommand.class::cast)
                .map(SyncCreateHotelCommandHandler::toApplicationCommand)
                .ifPresent(createHotel::execute));
  }
}
