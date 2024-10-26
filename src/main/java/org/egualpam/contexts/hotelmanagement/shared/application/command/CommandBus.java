package org.egualpam.contexts.hotelmanagement.shared.application.command;

@FunctionalInterface
public interface CommandBus {
    void publish(Command command);
}
