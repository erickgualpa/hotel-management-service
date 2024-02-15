package org.egualpam.services.hotel.rating.infrastructure.cqrs;

import org.egualpam.services.hotel.rating.application.shared.Command;

public class SimpleCommandBus implements CommandBus {
    @Override
    public void publish(Command command) {
        command.execute();
    }
}
