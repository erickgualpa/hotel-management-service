package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.shared.Command;

public interface CommandBus {
    CommandBuilder commandBuilder();

    void publish(Command command);
}
