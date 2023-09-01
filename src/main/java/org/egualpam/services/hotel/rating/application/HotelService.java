package org.egualpam.services.hotel.rating.application;

import org.egualpam.services.hotel.rating.domain.Hotel;

import java.util.List;

public interface HotelService {
    List<Hotel> findByQueryAndSortedByRatingAverage(HotelQuery query);
}
