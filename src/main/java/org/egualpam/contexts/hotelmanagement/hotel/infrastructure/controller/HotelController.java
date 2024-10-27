package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.controller;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.FindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.OneHotel;
import org.egualpam.contexts.hotelmanagement.hotel.domain.exceptions.PriceRangeValuesSwapped;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.QueryBus;
import org.egualpam.contexts.hotelmanagement.shared.domain.exceptions.InvalidUniqueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class HotelController {

  private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

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

  @PostMapping(value = "/query")
  public ResponseEntity<QueryHotelResponse> queryHotels(@RequestBody QueryHotelRequest request) {
    Query findHotelsQuery =
        new FindHotelsQuery(
            request.location(),
            Optional.ofNullable(request.priceRange())
                .map(QueryHotelRequest.PriceRange::begin)
                .orElse(null),
            Optional.ofNullable(request.priceRange())
                .map(QueryHotelRequest.PriceRange::end)
                .orElse(null));

    final ManyHotels manyHotels;
    try {
      manyHotels = (ManyHotels) queryBus.publish(findHotelsQuery);
    } catch (PriceRangeValuesSwapped e) {
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      logger.error(
          String.format("An error occurred while processing the request [%s]", request), e);
      return ResponseEntity.internalServerError().build();
    }
    return ResponseEntity.ok(mapIntoResponse(manyHotels.hotels()));
  }

  private QueryHotelResponse mapIntoResponse(List<ManyHotels.Hotel> hotels) {
    return new QueryHotelResponse(
        hotels.stream()
            .map(
                h ->
                    new QueryHotelResponse.Hotel(
                        h.identifier(),
                        h.name(),
                        h.description(),
                        h.location(),
                        h.price(),
                        h.imageURL(),
                        h.averageRating()))
            .toList());
  }
}
