package org.egualpam.services.hotel.rating.application;

import java.util.List;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;

public interface HotelRepository {
    List<Hotel> findHotelsMatchingQuery(HotelQuery query);
}
