package org.egualpam.services.hotel.rating.domain;

import org.egualpam.services.hotel.rating.application.HotelQuery;

import java.util.List;

public interface RatedHotelRepository {
    List<RatedHotel> findHotelsMatchingQuery(HotelQuery query);
}
