package org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.simple;

import java.util.Map;
import java.util.Optional;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.CommandBus;

public final class SimpleCommandBus implements CommandBus {

  private final Map<Class<? extends Command>, CommandHandler> handlers;

  public SimpleCommandBus(Map<Class<? extends Command>, CommandHandler> handlers) {
    this.handlers = handlers;
  }

  @Override
  public void publish(Command command) {
    Optional.ofNullable(handlers.get(command.getClass()))
        .orElseThrow(() -> new CommandHandlerNotFound(command))
        .handle(command);
  }
}
