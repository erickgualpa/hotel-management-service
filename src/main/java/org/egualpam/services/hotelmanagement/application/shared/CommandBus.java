package org.egualpam.services.hotelmanagement.application.shared;

@FunctionalInterface
public interface CommandBus {
    void publish(Command command);
}
