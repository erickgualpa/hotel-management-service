package org.egualpam.services.hotel.rating.controller;

import java.util.List;
import org.egualpam.services.hotel.rating.domain.entity.RatedHotel;

public interface HotelService {
    List<RatedHotel> findHotelsMatchingQuery(HotelQuery query);
}
