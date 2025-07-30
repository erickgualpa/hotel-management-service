package org.egualpam.contexts.hotelmanagement.room.infrastructure.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms")
public class GetRoomAvailabilityController {

  @GetMapping(value = "/{roomId}/availability")
  public ResponseEntity<FindRoomAvailabilityResponse> findRoomAvailability(
      @PathVariable String roomId) {
    final var response =
        new FindRoomAvailabilityResponse(
            List.of(new FindRoomAvailabilityResponse.AvailableDay(1, 8, 2025)));

    return ResponseEntity.ok().body(response);
  }
}
