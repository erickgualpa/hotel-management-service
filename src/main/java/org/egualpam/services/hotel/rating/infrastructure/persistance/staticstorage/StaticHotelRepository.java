package org.egualpam.services.hotel.rating.infrastructure.persistance.staticstorage;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Hotel;
import org.egualpam.services.hotel.rating.infrastructure.persistance.entity.Location;
import org.springframework.stereotype.Component;

@Component
public final class StaticHotelRepository {

    private static final List<Hotel> inMemoryHotels;

    static {
        inMemoryHotels = new ArrayList<>();
        inMemoryHotels.add(
                new Hotel(
                        "AMZ_HOTEL",
                        "Amazing hotel",
                        "Eloquent description",
                        new Location("BCN", "Barcelona"),
                        200,
                        "amz-hotel-image.com"));

        inMemoryHotels.add(
                new Hotel(
                        "AMZ_ROME_HOTEL",
                        "Amazing Rome hotel",
                        "Eloquent description",
                        new Location("ROM", "Rome"),
                        400,
                        "amz-rome-hotel-image.com"));

        inMemoryHotels.add(
                new Hotel(
                        "AMZ_BERLIN_HOTEL",
                        "Amazing Berlin hotel",
                        "Eloquent description",
                        new Location("BER", "Berlin"),
                        75,
                        "amz-berlin-hotel-image.com"));

        inMemoryHotels.add(
                new Hotel(
                        "NON_AMZ_HOTEL",
                        "Non Amazing hotel",
                        "Eloquent description",
                        new Location("BCN", "Barcelona"),
                        20,
                        "non-amz-hotel-image.com"));

        inMemoryHotels.add(
                new Hotel(
                        "MEDIUM_AMZ_HOTEL",
                        "Medium Amazing hotel",
                        "Eloquent description",
                        new Location("BCN", "Barcelona"),
                        90,
                        "medium-amz-hotel-image.com"));
    }

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
