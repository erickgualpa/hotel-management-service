package org.egualpam.services.hotel.rating.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.hotels.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.application.hotels.HotelDto;
import org.egualpam.services.hotel.rating.application.hotels.InvalidPriceRange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class HotelController {

    private final FindHotelsByRatingAverage findHotelsByRatingAverage;

    @PostMapping(value = "/query")
    public ResponseEntity<List<HotelDto>> queryHotels(@RequestBody Query query) {
        try {
            List<HotelDto> hotels =
                    findHotelsByRatingAverage.executeV2(
                            buildQueryFilters(query)
                    );
            return ResponseEntity.ok(hotels);
        } catch (InvalidPriceRange e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Map<String, String> buildQueryFilters(Query query) {
        Map<String, String> queryFilters = new HashMap<>();

        Optional.ofNullable(query.location())
                .ifPresent(
                        l -> queryFilters.put("location", l)
                );

        Optional.ofNullable(query.priceRange())
                .map(PriceRange::begin)
                .map(Object::toString)
                .ifPresent(
                        prb -> queryFilters.put("priceRangeBegin", prb)
                );

        Optional.ofNullable(query.priceRange())
                .map(PriceRange::end)
                .map(Objects::toString)
                .ifPresent(
                        pre -> queryFilters.put("priceRangeEnd", pre)
                );

        return queryFilters;
    }
}
