package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;

public class CommandHandlerNotFound extends RuntimeException {
  public CommandHandlerNotFound(Command command) {
    super(
        "No command handler found for command: [%s]".formatted(command.getClass().getSimpleName()));
  }
}
