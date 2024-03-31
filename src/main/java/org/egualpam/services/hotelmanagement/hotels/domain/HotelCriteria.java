package org.egualpam.services.hotelmanagement.hotels.domain;

import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;
import org.egualpam.services.hotelmanagement.shared.domain.exception.RequiredPropertyIsMissing;

import java.util.Optional;

public final class HotelCriteria implements Criteria {

    // TODO: Amend use of optionals
    private final Optional<UniqueId> hotelId;
    private final Optional<Location> location;
    private final Optional<PriceRange> priceRange;

    public HotelCriteria(String hotelId) {
        this.hotelId = Optional.ofNullable(hotelId).map(UniqueId::new);
        this.location = Optional.empty();
        this.priceRange = Optional.empty();
    }

    public HotelCriteria(
            Optional<String> location,
            Optional<Integer> minPrice,
            Optional<Integer> maxPrice
    ) {
        this.hotelId = Optional.empty();
        this.location = location.map(Location::new);
        this.priceRange = minPrice.isPresent() || maxPrice.isPresent()
                ? Optional.of(new PriceRange(minPrice.map(Price::new), maxPrice.map(Price::new)))
                : Optional.empty();
    }

    public UniqueId getHotelId() {
        return hotelId.orElseThrow(RequiredPropertyIsMissing::new);
    }

    public Optional<Location> getLocation() {
        return location;
    }

    public Optional<PriceRange> getPriceRange() {
        return priceRange;
    }
}
