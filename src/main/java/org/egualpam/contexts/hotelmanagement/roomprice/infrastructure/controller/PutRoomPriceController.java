package org.egualpam.contexts.hotelmanagement.roomprice.infrastructure.controller;

import static org.springframework.http.ResponseEntity.noContent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/room-prices")
public class PutRoomPriceController {

  @PutMapping
  public ResponseEntity<Void> updateRoomPrice() {
    return noContent().build();
  }
}
