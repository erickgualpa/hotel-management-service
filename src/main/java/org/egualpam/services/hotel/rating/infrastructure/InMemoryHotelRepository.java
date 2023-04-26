package org.egualpam.services.hotel.rating.infrastructure;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.application.HotelRepository;
import org.egualpam.services.hotel.rating.controller.HotelQuery;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.infrastructure.entity.Hotel;
import org.egualpam.services.hotel.rating.infrastructure.entity.Location;
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

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {
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
                .map(this::mapToRatedHotel)
                .collect(Collectors.toList());
    }

    private RatedHotel mapToRatedHotel(Hotel hotel) {
        return new RatedHotel(
                hotel.getIdentifier(),
                hotel.getName(),
                hotel.getDescription(),
                new org.egualpam.services.hotel.rating.domain.HotelLocation(
                        hotel.getLocation().getIdentifier(), hotel.getLocation().getName()),
                hotel.getTotalPrice(),
                hotel.getImageURL());
    }
}
