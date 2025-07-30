package org.egualpam.contexts.hotelmanagement.room.infrastructure.controller;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.internalServerError;

import org.egualpam.contexts.hotelmanagement.room.application.query.ManyAvailableDays;
import org.egualpam.contexts.hotelmanagement.room.infrastructure.cqrs.query.simple.SyncFindRoomNextMonthAvailabilityQuery;
import org.egualpam.contexts.hotelmanagement.shared.infrastructure.cqrs.query.QueryBus;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rooms")
public class GetRoomAvailabilityController {

  private final Logger logger = getLogger(this.getClass());
  private final QueryBus queryBus;

  public GetRoomAvailabilityController(QueryBus queryBus) {
    this.queryBus = queryBus;
  }

  @GetMapping(value = "/{roomId}/availability")
  public ResponseEntity<FindRoomAvailabilityResponse> findRoomAvailability(
      @PathVariable String roomId) {
    try {
      SyncFindRoomNextMonthAvailabilityQuery query =
          new SyncFindRoomNextMonthAvailabilityQuery(roomId);
      final var manyAvailableDays = (ManyAvailableDays) queryBus.publish(query);

      final var results =
          manyAvailableDays.availableDays().stream()
              .map(
                  availableDay ->
                      new FindRoomAvailabilityResponse.AvailableDay(
                          availableDay.day(), availableDay.month(), availableDay.year()))
              .toList();

      return ResponseEntity.ok().body(new FindRoomAvailabilityResponse(results));
    } catch (RuntimeException e) {
      logger.error("An error occurred while processing the request with room id: [{}]", roomId, e);
      return internalServerError().build();
    }
  }
}
