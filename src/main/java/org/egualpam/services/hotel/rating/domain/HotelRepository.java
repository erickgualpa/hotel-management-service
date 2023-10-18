package org.egualpam.services.hotel.rating.domain;

import org.egualpam.services.hotel.rating.application.HotelQuery;

import java.util.List;

public abstract class HotelRepository {

    public abstract List<Hotel> findHotelsMatchingQuery(HotelQuery query);

    protected final Hotel mapIntoEntity(
            String identifier,
            String name,
            String description,
            String location,
            Integer totalPrice,
            String imageURL
    ) {
        return new Hotel(
                identifier,
                name,
                description,
                location,
                totalPrice,
                imageURL
        );
    }
}
