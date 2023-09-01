package org.egualpam.services.hotel.rating.domain;

import org.egualpam.services.hotel.rating.application.HotelQuery;

import java.util.List;
import java.util.UUID;

public abstract class RatedHotelRepository {

    abstract public List<RatedHotel> findHotelsMatchingQuery(HotelQuery query);

    protected RatedHotel buildEntity(
            String identifier,
            String name,
            String description,
            String locationName,
            Integer totalPrice,
            String imageURL
    ) {
        return new RatedHotel(
                identifier,
                name,
                description,
                // TODO: Decide what how 'Location' will be managed (Entity or Value)
                new Location(UUID.randomUUID().toString(), locationName),
                totalPrice,
                imageURL
        );
    }
}
