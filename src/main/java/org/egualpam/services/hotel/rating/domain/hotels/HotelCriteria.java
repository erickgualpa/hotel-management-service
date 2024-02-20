package org.egualpam.services.hotel.rating.domain.hotels;

import org.egualpam.services.hotel.rating.domain.shared.Criteria;

import java.util.Optional;

public final class HotelCriteria implements Criteria {
    private final Optional<Location> location;
    private final PriceRange priceRange;

    public HotelCriteria(Optional<Location> location, PriceRange priceRange) {
        this.location = location;
        this.priceRange = priceRange;
    }

    public Optional<Location> getLocation() {
        return location;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }
}
