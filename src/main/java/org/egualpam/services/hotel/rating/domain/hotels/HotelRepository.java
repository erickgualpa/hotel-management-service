package org.egualpam.services.hotel.rating.domain.hotels;

import java.util.List;
import java.util.Optional;

public abstract class HotelRepository {

    public abstract List<Hotel> findHotels(Optional<Location> location,
                                           Optional<Price> minPrice,
                                           Optional<Price> maxPrice
    );

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
