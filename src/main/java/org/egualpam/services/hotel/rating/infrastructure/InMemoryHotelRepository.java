package org.egualpam.services.hotel.rating.infrastructure;

import java.util.ArrayList;
import java.util.List;
import org.egualpam.services.hotel.rating.application.HotelRepository;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Hotel;
import org.egualpam.services.hotel.rating.domain.HotelLocation;
import org.springframework.stereotype.Component;

@Component
public final class InMemoryHotelRepository implements HotelRepository {

    private static final List<Hotel> inMemoryHotels;

    static {
        inMemoryHotels = new ArrayList<>();
        inMemoryHotels.add(
                new Hotel(
                        "AMZ_HOTEL",
                        "Amazing hotel",
                        "Eloquent description",
                        new HotelLocation("BCN", "Barcelona"),
                        200,
                        "amz-hotel-image.com"));
    }

    @Override
    public List<Hotel> findHotelsMatchingQuery(HotelQuery query) {
        return null;
    }
}
