package org.egualpam.services.hotel.rating.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.hotels.HotelQueryAssistant;
import org.egualpam.services.hotel.rating.domain.hotels.exception.PriceRangeValuesSwapped;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class HotelController {

    private final HotelQueryAssistant hotelQueryFactory;

    @PostMapping(value = "/query")
    public ResponseEntity<QueryHotelResponse> queryHotels(@RequestBody QueryHotelRequest query) {
        try {
            Optional<String> locationFilter = Optional.ofNullable(query.location());
            Optional<Integer> minPriceFilter =
                    Optional.ofNullable(query.priceRange())
                            .map(QueryHotelRequest.PriceRange::begin);
            Optional<Integer> maxPriceFilter =
                    Optional.ofNullable(query.priceRange())
                            .map(QueryHotelRequest.PriceRange::end);

            List<QueryHotelResponse.Hotel> hotels =
                    hotelQueryFactory
                            .findHotelsQuery(
                                    locationFilter,
                                    minPriceFilter,
                                    maxPriceFilter

                            )
                            .get()
                            .stream()
                            .map(
                                    h -> new QueryHotelResponse.Hotel(
                                            h.identifier(),
                                            h.name(),
                                            h.description(),
                                            h.location(),
                                            h.totalPrice(),
                                            h.imageURL(),
                                            h.averageRating()
                                    )
                            )
                            .toList();
            return ResponseEntity.ok(new QueryHotelResponse(hotels));
        } catch (PriceRangeValuesSwapped e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
