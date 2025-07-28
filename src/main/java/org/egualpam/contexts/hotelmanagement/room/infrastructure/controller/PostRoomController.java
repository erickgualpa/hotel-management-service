package org.egualpam.contexts.hotelmanagement.room.infrastructure.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms")
public class PostRoomController {

  @PostMapping
  public ResponseEntity<Void> postRoom() {
    return status(CREATED).build();
  }
}
