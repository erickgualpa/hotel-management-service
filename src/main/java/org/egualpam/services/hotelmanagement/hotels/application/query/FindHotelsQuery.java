package org.egualpam.services.hotelmanagement.hotels.application.query;

import org.egualpam.services.hotelmanagement.shared.application.query.Query;

import java.util.Optional;

public final class FindHotelsQuery implements Query {
    private final String location;
    private final Integer minPrice;
    private final Integer maxPrice;

    public FindHotelsQuery(
            String location,
            Integer minPrice,
            Integer maxPrice) {
        this.location = location;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }

    public Optional<Integer> getMinPrice() {
        return Optional.ofNullable(minPrice);
    }

    public Optional<Integer> getMaxPrice() {
        return Optional.ofNullable(maxPrice);
    }
}
