package org.egualpam.contexts.hotelmanagement.shared.application.command;

public interface InternalEventBus {
  void publish(InternalEvent event);
}
