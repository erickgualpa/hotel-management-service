package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple;

import java.util.HashMap;
import java.util.Map;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;

public class SimpleCommandBusConfiguration {
  private final Map<Class<? extends Command>, CommandHandler> handlers = new HashMap<>();

  public SimpleCommandBusConfiguration withHandler(
      Class<? extends Command> type, CommandHandler handler) {
    handlers.put(type, handler);
    return this;
  }

  public Map<Class<? extends Command>, CommandHandler> getHandlers() {
    return handlers;
  }
}
