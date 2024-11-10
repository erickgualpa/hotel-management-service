package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.command.simple;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.CreateHotel;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.CreateHotelCommand;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple.CommandHandler;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
public class CreateHotelCommandHandler implements CommandHandler {

  private final TransactionTemplate transactionTemplate;
  private final CreateHotel createHotel;

  @Override
  public void handle(Command command) {
    transactionTemplate.executeWithoutResult(
        ts ->
            Optional.of(command)
                .filter(CreateHotelCommand.class::isInstance)
                .map(CreateHotelCommand.class::cast)
                .ifPresent(createHotel::execute));
  }
}
