package org.egualpam.contexts.hotelmanagement.shared.application.command;

// TODO: Check if it is still needed to have a internal event bus
public interface InternalEventBus {
  void publish(InternalEvent event);
}
