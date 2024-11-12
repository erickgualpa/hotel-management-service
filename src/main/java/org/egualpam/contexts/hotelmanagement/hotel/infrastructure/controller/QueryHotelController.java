package org.egualpam.contexts.hotelmanagement.hotel.infrastructure.controller;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotelmanagement.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotelmanagement.hotel.domain.PriceRangeValuesSwapped;
import org.egualpam.contexts.hotelmanagement.hotel.infrastructure.cqrs.query.simple.SyncFindHotelsQuery;
import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;
import org.egualpam.contexts.hotelmanagement.shared.application.query.QueryBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class QueryHotelController {

  private static final Logger logger = LoggerFactory.getLogger(QueryHotelController.class);

  private final QueryBus queryBus;

  @PostMapping(value = "/query")
  public ResponseEntity<QueryHotelResponse> queryHotels(@RequestBody QueryHotelRequest request) {
    Query findHotelsQuery = toQuery(request);

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

  private Query toQuery(QueryHotelRequest request) {
    String location = request.location();
    Integer minPrice =
        Optional.of(request)
            .map(QueryHotelRequest::priceRange)
            .map(QueryHotelRequest.PriceRange::begin)
            .orElse(null);
    Integer maxPrice =
        Optional.of(request)
            .map(QueryHotelRequest::priceRange)
            .map(QueryHotelRequest.PriceRange::end)
            .orElse(null);

    return new SyncFindHotelsQuery(location, minPrice, maxPrice);
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
