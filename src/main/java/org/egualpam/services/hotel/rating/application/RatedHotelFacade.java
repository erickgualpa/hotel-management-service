package org.egualpam.services.hotel.rating.application;

import java.util.ArrayList;
import java.util.List;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.controller.HotelService;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.springframework.stereotype.Component;

@Component
public class RatedHotelFacade implements HotelService {

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {
        // TODO: Add unit testing and implement!
        return List.of();
    }
}
