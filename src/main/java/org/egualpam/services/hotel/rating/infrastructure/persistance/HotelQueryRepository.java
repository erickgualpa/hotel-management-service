package org.egualpam.services.hotel.rating.infrastructure.persistance;

import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;

public interface HotelQueryRepository {

    List<Hotel> findHotelsMatchingQuery(HotelQuery hotelQuery);

    void registerHotel(Hotel hotel);
}
