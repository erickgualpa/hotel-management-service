package org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command;

@FunctionalInterface
public interface CommandBus {
  void publish(Command command);
}
