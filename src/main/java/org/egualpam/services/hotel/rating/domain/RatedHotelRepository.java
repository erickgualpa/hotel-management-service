package org.egualpam.services.hotel.rating.domain;

import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelQuery;

public interface RatedHotelRepository {
    List<RatedHotel> findHotelsMatchingQuery(HotelQuery query);
}
