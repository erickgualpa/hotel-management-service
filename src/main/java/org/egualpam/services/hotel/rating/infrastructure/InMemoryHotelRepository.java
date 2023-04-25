package org.egualpam.services.hotel.rating.infrastructure;

import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelRepository;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.springframework.stereotype.Component;

@Component
public final class InMemoryHotelRepository implements HotelRepository {

    @Override
    public List<Hotel> findHotelsMatchingQuery(HotelQuery query) {
        return null;
    }
}
