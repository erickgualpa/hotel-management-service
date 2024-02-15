package org.egualpam.services.hotel.rating.infrastructure.cqrs;

@FunctionalInterface
public interface CommandBus {
    void publish(Command command);
}
