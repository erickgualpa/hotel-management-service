package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.CreateHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.CreateHotelCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class AsyncCreateHotelCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final CreateHotel createHotel;

  private static CreateHotelCommand toApplicationCommand(AsyncCreateHotelCommand cmd) {
    return new CreateHotelCommand(
        cmd.id(), cmd.name(), cmd.description(), cmd.location(), cmd.price(), cmd.imageURL());
  }

  @Override
  public void handle(Command command) {
    CompletableFuture.runAsync(
        () ->
            transactionTemplate.executeWithoutResult(
                ts ->
                    Optional.of(command)
                        .filter(AsyncCreateHotelCommand.class::isInstance)
                        .map(AsyncCreateHotelCommand.class::cast)
                        .map(AsyncCreateHotelCommandHandler::toApplicationCommand)
                        .ifPresent(createHotel::execute)));
  }
}
