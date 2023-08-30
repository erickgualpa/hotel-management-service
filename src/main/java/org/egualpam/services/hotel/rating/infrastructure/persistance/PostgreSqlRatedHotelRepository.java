package org.egualpam.services.hotel.rating.infrastructure.persistance;

import org.egualpam.services.hotel.rating.application.HotelQuery;
import org.egualpam.services.hotel.rating.domain.Location;
import org.egualpam.services.hotel.rating.domain.RatedHotel;
import org.egualpam.services.hotel.rating.domain.RatedHotelRepository;

import java.util.List;

public class PostgreSqlRatedHotelRepository implements RatedHotelRepository {

    @Override
    public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query) {
        // TODO: Implement!
        return List.of(
                new RatedHotel(
                        "some-identifier",
                        "some-name",
                        "some-description",
                        new Location(
                                "some-location-identifier",
                                "some-location-name"
                        ),
                        100,
                        "some-url.com")
        );
    }
}
