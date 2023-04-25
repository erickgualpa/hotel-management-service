package org.egualpam.services.hotel.rating.controller;

import java.util.List;
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
    public List<RatedHotel> queryHotels(@RequestBody HotelQuery query) {
        return service.findHotelsMatchingQuery(query);
    }
}
