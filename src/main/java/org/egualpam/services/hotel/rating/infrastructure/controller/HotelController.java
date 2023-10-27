package org.egualpam.services.hotel.rating.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.application.hotels.HotelFilters;
import org.egualpam.services.hotel.rating.domain.hotels.InvalidPriceRange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.LOCATION;
import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.PRICE_RANGE_BEGIN;
import static org.egualpam.services.hotel.rating.application.hotels.HotelFilters.PRICE_RANGE_END;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class HotelController {

    private final FindHotelsByRatingAverage findHotelsByRatingAverage;

    @PostMapping(value = "/query")
    public ResponseEntity<List<HotelDto>> queryHotels(@RequestBody Query query) {
        try {
            List<HotelDto> hotels = findHotelsByRatingAverage.execute(
                    buildHotelFilters(query)
            );
            return ResponseEntity.ok(hotels);
        } catch (InvalidPriceRange e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Map<HotelFilters, String> buildHotelFilters(Query query) {
        Map<HotelFilters, String> hotelFilters = new EnumMap<>(HotelFilters.class);

        Optional.ofNullable(query.location())
                .ifPresent(
                        l -> hotelFilters.put(LOCATION, l)
                );

        Optional.ofNullable(query.priceRange())
                .map(PriceRange::begin)
                .map(Object::toString)
                .ifPresent(
                        prb -> hotelFilters.put(PRICE_RANGE_BEGIN, prb)
                );

        Optional.ofNullable(query.priceRange())
                .map(PriceRange::end)
                .map(Objects::toString)
                .ifPresent(
                        pre -> hotelFilters.put(PRICE_RANGE_END, pre)
                );

        return hotelFilters;
    }
}
