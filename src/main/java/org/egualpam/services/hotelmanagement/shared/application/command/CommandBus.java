package org.egualpam.services.hotelmanagement.shared.application.command;

@FunctionalInterface
public interface CommandBus {
    void publish(Command command);
}
