package org.egualpam.services.hotel.rating.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.hotels.Filters;
import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByAverageRating;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
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

    private final FindHotelsByAverageRating findHotelsByAverageRating;

    @PostMapping(value = "/query")
    public ResponseEntity<QueryHotelResponse> queryHotels(@RequestBody QueryHotelRequest query) {
        Filters filters = new Filters(
                query.location(),
                Optional.ofNullable(query.priceRange())
                        .map(QueryHotelRequest.PriceRange::begin)
                        .orElse(null),
                Optional.ofNullable(query.priceRange())
                        .map(QueryHotelRequest.PriceRange::end)
                        .orElse(null)
        );
        try {
            List<QueryHotelResponse.Hotel> hotels =
                    findHotelsByAverageRating.execute(filters).stream()
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
        } catch (InvalidPriceRange e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
