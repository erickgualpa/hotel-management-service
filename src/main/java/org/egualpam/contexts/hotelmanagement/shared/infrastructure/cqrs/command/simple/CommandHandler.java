package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple;

import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.Command;

@FunctionalInterface
public interface CommandHandler {
  void handle(Command command);
}
