package org.egualpam.services.hotel.rating.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.application.HotelService;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public final class HotelController {

    private final HotelService service;

    @PostMapping(value = "/query")
    public List<RatedHotel> queryHotels(@RequestBody Query query) {

        HotelQuery hotelQuery =
                HotelQuery.create()
                        .withLocation(query.getLocation())
                        .withPriceRange(query.getMinPrice(), query.getMaxPrice())
                        .build();

        return service.findByQueryAndSortedByRatingAverage(hotelQuery);
    }
}