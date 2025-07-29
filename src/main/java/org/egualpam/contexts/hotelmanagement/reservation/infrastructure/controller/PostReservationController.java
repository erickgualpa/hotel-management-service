package org.egualpam.contexts.hotelmanagement.reservation.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reservations")
public class PostReservationController {

  @PostMapping
  public ResponseEntity<Void> postReservation(@RequestBody PostReservationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
