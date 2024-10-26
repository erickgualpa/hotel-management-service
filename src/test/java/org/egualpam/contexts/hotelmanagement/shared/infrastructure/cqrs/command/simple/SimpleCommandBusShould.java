package org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.simple;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.application.command.CommandBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleCommandBusShould {

  private CommandBus testee;

  @BeforeEach
  void setUp() {
    testee = new SimpleCommandBus(Collections.emptyMap());
  }

  @Test
  void throwException_whenCommandNotMatchesAnyHandler() {
    Command command = new Command() {};
    assertThrows(CommandHandlerNotFound.class, () -> testee.publish(command));
  }
}
