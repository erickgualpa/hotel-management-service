package org.egualpam.services.hotelmanagement.shared.application;

@FunctionalInterface
public interface CommandBus {
    void publish(Command command);
}
