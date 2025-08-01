package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.controller;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.status;

import org.egualpam.contexts.hotelmanagement.reservation.infrastructure.cqrs.command.simple.SyncCreateReservationCommand;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reservations")
public class PostReservationController {

  private final CommandBus commandBus;
  private final Logger logger = getLogger(this.getClass());

  public PostReservationController(CommandBus commandBus) {
    this.commandBus = commandBus;
  }

  @PostMapping
  public ResponseEntity<Void> createReservation(@RequestBody PostReservationRequest request) {
    try {
      final var command =
          new SyncCreateReservationCommand(
              request.id(), request.roomId(), request.from(), request.to());
      commandBus.publish(command);
      return status(CREATED).build();
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the request [{}]", request, e);
      return internalServerError().build();
    }
  }
}
