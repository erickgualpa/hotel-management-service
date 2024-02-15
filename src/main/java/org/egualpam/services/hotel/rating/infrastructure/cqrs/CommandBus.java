package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.shared.Command;

public interface CommandBus {
    void publish(Command command);
}
