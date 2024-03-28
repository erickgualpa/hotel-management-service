package org.egualpam.services.hotelmanagement.hotels.domain;

import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;

import java.util.Optional;

public final class HotelCriteria implements Criteria {

    private final Optional<UniqueId> hotelId;
    private final Optional<Location> location;
    private final PriceRange priceRange;

    public HotelCriteria(String hotelId) {
        this.hotelId = Optional.of(hotelId).map(UniqueId::new);
        this.location = Optional.empty();
        this.priceRange = new PriceRange(
                Optional.empty(),
                Optional.empty()
        );
    }

    public HotelCriteria(
            Optional<String> location,
            Optional<Integer> minPrice,
            Optional<Integer> maxPrice
    ) {
        this.hotelId = Optional.empty();
        this.location = location.map(Location::new);
        this.priceRange = new PriceRange(
                minPrice.map(Price::new),
                maxPrice.map(Price::new)
        );
    }

    public Optional<Location> getLocation() {
        return location;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public Optional<UniqueId> getHotelId() {
        return hotelId;
    }
}