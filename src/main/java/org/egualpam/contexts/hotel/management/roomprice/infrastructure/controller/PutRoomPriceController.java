package org.egualpam.contexts.hotel.management.roomprice.infrastructure.controller;

import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.noContent;

import org.egualpam.contexts.hotel.management.roomprice.infrastructure.cqrs.command.simple.SyncUpdateRoomPriceCommand;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.CommandBus;
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
        new SyncUpdateRoomPriceCommand(
            request.hotelId(), request.roomType(), request.priceAmount());
    try {
      commandBus.publish(command);
      return noContent().build();
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the request: [{}]", request, e);
      return internalServerError().build();
    }
  }
}
