package org.egualpam.services.hotel.rating.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.hotels.Filters;
import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
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

    private final FindHotelsByRatingAverage findHotelsByRatingAverage;

    @PostMapping(value = "/query")
    public ResponseEntity<List<HotelDto>> queryHotels(@RequestBody Query query) {
        Filters filters = new Filters(
                query.location(),
                Optional.ofNullable(query.priceRange())
                        .map(PriceRange::begin)
                        .orElse(null),
                Optional.ofNullable(query.priceRange())
                        .map(PriceRange::end)
                        .orElse(null)
        );
        try {
            List<HotelDto> hotels = findHotelsByRatingAverage.execute(filters);
            return ResponseEntity.ok(hotels);
        } catch (InvalidPriceRange e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
