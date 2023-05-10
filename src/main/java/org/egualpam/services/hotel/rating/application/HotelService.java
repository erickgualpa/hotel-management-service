package org.egualpam.services.hotel.rating.application;

import java.util.List;
import org.egualpam.services.hotel.rating.domain.RatedHotel;

public interface HotelService {
    List<RatedHotel> findHotelsMatchingQuery(HotelQuery query);
}
