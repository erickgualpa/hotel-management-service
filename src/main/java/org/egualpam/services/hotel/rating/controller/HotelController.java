package org.egualpam.services.hotel.rating.controller;

import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.application.HotelService;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

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
                        // TODO: Uncomment when checkIn/checkOut filtering introduced
                        /*.withCheckIn(query.getCheckIn())
                        .withCheckOut(query.getCheckOut())*/
                        .withPriceRange(query.getMinPrice(), query.getMaxPrice())
                        .build();

        return service.findByQueryAndSortedByRatingAverage(hotelQuery);
    }
}
