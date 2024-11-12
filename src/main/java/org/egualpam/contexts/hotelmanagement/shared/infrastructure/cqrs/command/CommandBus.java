package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command;

@FunctionalInterface
public interface CommandBus {
  void publish(Command command);
}
