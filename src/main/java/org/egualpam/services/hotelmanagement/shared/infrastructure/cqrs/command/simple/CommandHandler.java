package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple;


import org.egualpam.services.hotelmanagement.shared.application.command.Command;

@FunctionalInterface
public interface CommandHandler {
    void handle(Command command);
}
