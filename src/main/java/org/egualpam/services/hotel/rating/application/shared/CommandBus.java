package org.egualpam.services.hotel.rating.application.shared;

@FunctionalInterface
public interface CommandBus {
    void publish(Command command);
}
