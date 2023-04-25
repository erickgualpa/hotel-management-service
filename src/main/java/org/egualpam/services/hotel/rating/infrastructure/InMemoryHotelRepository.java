package org.egualpam.services.hotel.rating.infrastructure;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
        return inMemoryHotels.stream()
                .filter(
                        hotel ->
                                isNull(query.getLocation())
                                        || hotel.getLocation()
                                                .getName()
                                                .equals(query.getLocation()))
                .filter(
                        hotel ->
                                isNull(query.getPriceRange())
                                        || (hotel.getTotalPrice()
                                                        >= query.getPriceRange().getBegin()
                                                && hotel.getTotalPrice()
                                                        <= query.getPriceRange().getEnd()))
                .collect(Collectors.toList());
    }
}
