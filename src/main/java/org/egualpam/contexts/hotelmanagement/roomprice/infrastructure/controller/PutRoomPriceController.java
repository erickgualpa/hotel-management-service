package org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.controller;

import static org.springframework.http.ResponseEntity.noContent;

import org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.cqrs.command.simple.SyncUpdateRoomPriceCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/room-prices")
public class PutRoomPriceController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final CommandBus commandBus;

  public PutRoomPriceController(CommandBus commandBus) {
    this.commandBus = commandBus;
  }

  @PutMapping
  public ResponseEntity<Void> updateRoomPrice(@RequestBody UpdateRoomPriceRequest request) {
    final var command =
        new SyncUpdateRoomPriceCommand(request.id(), request.hotelId(), request.roomType());
    try {
      commandBus.publish(command);
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the request: [{}]", request, e);
    }
    return noContent().build();
  }
}
