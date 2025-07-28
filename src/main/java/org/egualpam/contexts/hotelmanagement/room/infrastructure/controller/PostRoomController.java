package org.egualpam.contexts.hotelmanagement.room.infrastructure.controller;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

import org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.command.simple.SyncCreateRoomCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms")
public class PostRoomController {

  private final Logger logger = getLogger(this.getClass());
  private final CommandBus commandBus;

  public PostRoomController(CommandBus commandBus) {
    this.commandBus = commandBus;
  }

  @PostMapping
  public ResponseEntity<Void> postRoom(@RequestBody PostRoomRequest request) {
    final var command = new SyncCreateRoomCommand(request.id(), request.type(), request.hotelId());

    try {
      commandBus.publish(command);
    } catch (RuntimeException e) {
      logger.error("Unexpected error while processing the request [{}]", request, e);
    }

    return status(CREATED).build();
  }
}
