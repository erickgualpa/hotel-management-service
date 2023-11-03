package org.egualpam.services.hotel.rating.domain.hotels;

import org.egualpam.services.hotel.rating.domain.shared.Identifier;

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
                new Identifier(identifier),
                new HotelName(name),
                new HotelDescription(description),
                new Location(location),
                new Price(totalPrice),
                new ImageURL(imageURL)
        );
    }
}
