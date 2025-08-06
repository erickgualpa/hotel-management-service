package org.egualpam.contexts.hotel.management.hotel.infrastructure.controller;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.internalServerError;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.management.hotel.infrastructure.cqrs.command.simple.AsyncCreateHotelCommand;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.Command;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.command.CommandBus;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/hotels")
@RequiredArgsConstructor
public final class PostHotelControllerV2 {

  private final Logger logger = getLogger(this.getClass());
  private final CommandBus commandBus;

  @PostMapping
  public ResponseEntity<Void> postHotel(@RequestBody PostHotelRequest request) {
    Command createHotelCommand =
        new AsyncCreateHotelCommand(
            request.id(),
            request.name(),
            request.description(),
            request.location(),
            request.price(),
            request.imageURL());

    try {
      commandBus.publish(createHotelCommand);
    } catch (RuntimeException e) {
      logger.error(format("An error occurred while processing the request [%s]", request), e);
      return internalServerError().build();
    }

    return accepted().build();
  }
}
