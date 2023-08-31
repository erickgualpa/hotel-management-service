package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.dto.Hotel;

import java.util.List;

// TODO: This is unused currently (just created to try some approaches)
public interface HotelQueryRepository {

    List<Hotel> findHotelsMatchingQuery(HotelQuery hotelQuery);
}
