package org.egualpam.contexts.hotel.shared.application.command;

// TODO: Check if it is still needed to have a internal event bus
public interface InternalEventBus {
  void publish(InternalEvent event);
}
