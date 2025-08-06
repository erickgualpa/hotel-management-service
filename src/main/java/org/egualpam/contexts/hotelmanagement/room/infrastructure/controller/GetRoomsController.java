package org.egualpam.contexts.hotelmanagement.room.infrastructure.controller;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.internalServerError;

import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.QueryBus;
import org.egualpam.contexts.hotelmanagement.room.application.query.ManyRooms;
import org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.query.simple.SyncFindRoomsQuery;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms")
public class GetRoomsController {

  private final Logger logger = getLogger(this.getClass());
  private final QueryBus queryBus;

  public GetRoomsController(QueryBus queryBus) {
    this.queryBus = queryBus;
  }

  @GetMapping
  public ResponseEntity<FindRoomsResponse> findRooms(
      @RequestParam String availableFrom, @RequestParam String availableTo) {
    try {
      SyncFindRoomsQuery query = new SyncFindRoomsQuery(availableFrom, availableTo);
      final var manyRooms = (ManyRooms) queryBus.publish(query);

      final var results =
          manyRooms.rooms().stream().map(room -> new FindRoomsResponse.Room(room.id())).toList();

      return ResponseEntity.ok().body(new FindRoomsResponse(results));
    } catch (RuntimeException e) {
      logger.error(
          "An error occurred while processing the request with availableFrom: [{}] and availableTo: [{}]",
          availableFrom,
          availableTo,
          e);
      return internalServerError().build();
    }
  }
}
