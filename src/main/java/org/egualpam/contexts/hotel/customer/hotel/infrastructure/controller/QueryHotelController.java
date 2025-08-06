package org.egualpam.contexts.hotel.customer.hotel.infrastructure.controller;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.egualpam.contexts.hotel.customer.hotel.application.query.ManyHotels;
import org.egualpam.contexts.hotel.customer.hotel.domain.PriceRangeValuesSwapped;
import org.egualpam.contexts.hotel.customer.hotel.infrastructure.cqrs.query.simple.SyncFindHotelsQuery;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.Query;
import org.egualpam.contexts.hotel.shared.infrastructure.cqrs.query.QueryBus;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class QueryHotelController {

  private final Logger logger = getLogger(this.getClass());
  private final QueryBus queryBus;

  @PostMapping(value = "/query")
  public ResponseEntity<QueryHotelResponse> queryHotels(@RequestBody QueryHotelRequest request) {
    Query findHotelsQuery = toQuery(request);

    final ManyHotels manyHotels;
    try {
      manyHotels = (ManyHotels) queryBus.publish(findHotelsQuery);
    } catch (PriceRangeValuesSwapped e) {
      return badRequest().build();
    } catch (RuntimeException e) {
      logger.error(format("An error occurred while processing the request [%s]", request), e);
      return internalServerError().build();
    }

    return ok(mapIntoResponse(manyHotels.hotels()));
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
