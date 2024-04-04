package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.command.simple;

import org.egualpam.services.hotelmanagement.shared.application.command.Command;
import org.egualpam.services.hotelmanagement.shared.application.command.CommandBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleCommandBusShould {

    private CommandBus testee;

    @BeforeEach
    void setUp() {
        testee = new SimpleCommandBus(Collections.emptyMap());
    }

    @Test
    void throwException_whenCommandNotMatchesAnyHandler() {
        Command command = new Command() {
        };
        assertThrows(CommandHandlerNotFound.class, () -> testee.publish(command));
    }
}