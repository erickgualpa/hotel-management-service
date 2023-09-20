package org.egualpam.services.hotel.rating.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.FindHotelsByRatingAverage;
import org.egualpam.services.hotel.rating.application.HotelDto;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/hotels")
@RequiredArgsConstructor
public final class HotelController {

    private final FindHotelsByRatingAverage findHotelsByRatingAverage;

    @PostMapping(value = "/query")
    public ResponseEntity<List<HotelDto>> queryHotels(@RequestBody Query query) {

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation(query.getLocation())
                        .withPriceRange(query.getMinPrice(), query.getMaxPrice())
                        .build();

        List<HotelDto> hotels = findHotelsByRatingAverage.execute(hotelQuery);

        return ResponseEntity.ok(hotels);
    }
}
