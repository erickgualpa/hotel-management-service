package org.egualpam.services.hotel.rating.domain;

import java.util.List;
import org.egualpam.services.hotel.rating.controller.HotelQuery;

public interface RatedHotelRepository {
    List<RatedHotel> findHotelsMatchingQuery(HotelQuery query);
}
