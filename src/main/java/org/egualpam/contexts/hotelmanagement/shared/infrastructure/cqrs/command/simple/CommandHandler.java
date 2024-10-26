package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple;


import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;

@FunctionalInterface
public interface CommandHandler {
    void handle(Command command);
}
