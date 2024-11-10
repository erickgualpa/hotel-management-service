package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.InvalidUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class GetHotelController {

  private static final Logger logger = LoggerFactory.getLogger(GetHotelController.class);

  private final QueryBus queryBus;

  @GetMapping(value = "/{hotelId}")
  public ResponseEntity<GetHotelResponse> getHotel(@PathVariable String hotelId) {
    Query findHotelQuery = new FindHotelQuery(hotelId);

    final OneHotel oneHotel;
    try {
      oneHotel = (OneHotel) queryBus.publish(findHotelQuery);
    } catch (InvalidUniqueId e) {
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      logger.error(
          String.format("An error occurred while processing the request [hotelId=%s]", hotelId), e);
      return ResponseEntity.internalServerError().build();
    }

    return oneHotel
        .hotel()
        .map(hotel -> ResponseEntity.ok(mapIntoResponse(hotel)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  private GetHotelResponse mapIntoResponse(OneHotel.Hotel hotel) {
    return new GetHotelResponse(
        new GetHotelResponse.Hotel(
            hotel.identifier(),
            hotel.name(),
            hotel.description(),
            hotel.location(),
            hotel.price(),
            hotel.imageURL(),
            hotel.averageRating()));
  }
}
