package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.command.CreateHotelCommand;
import org.egualpam.contexts.hotelmanagement.shared.application.command.Command;
import org.egualpam.contexts.hotelmanagement.shared.application.command.CommandBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class PostHotelController {

  private static final Logger logger = LoggerFactory.getLogger(PostHotelController.class);

  private final CommandBus commandBus;

  @PostMapping
  public ResponseEntity<Void> postHotel(@RequestBody PostHotelRequest request) {
    Command createHotelCommand =
        new CreateHotelCommand(
            request.id(),
            request.name(),
            request.description(),
            request.location(),
            request.price(),
            request.imageURL());

    try {
      commandBus.publish(createHotelCommand);
    } catch (RuntimeException e) {
      logger.error(
          String.format("An error occurred while processing the request [%s]", request), e);
      return ResponseEntity.internalServerError().build();
    }

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
