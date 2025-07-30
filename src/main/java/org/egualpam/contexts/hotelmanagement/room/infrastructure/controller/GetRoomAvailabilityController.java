package org.egualpam.contexts.hotelmanagement.room.infrastructure.controller;

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
    return ResponseEntity.ok().body(new FindRoomAvailabilityResponse());
  }
}
