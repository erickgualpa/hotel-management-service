package org.egualpam.services.hotel.rating.application.hotels;

import org.egualpam.services.hotel.rating.application.shared.Query;

import java.util.Optional;

public final class FindHotelsQuery implements Query {
    private final Optional<String> location;
    private final Optional<Integer> minPrice;
    private final Optional<Integer> maxPrice;

    public FindHotelsQuery(
            Optional<String> location,
            Optional<Integer> minPriceFilter,
            Optional<Integer> maxPrice) {
        this.location = location;
        this.minPrice = minPriceFilter;
        this.maxPrice = maxPrice;
    }

    public Optional<String> getLocation() {
        return location;
    }

    public Optional<Integer> getMinPrice() {
        return minPrice;
    }

    public Optional<Integer> getMaxPrice() {
        return maxPrice;
    }
}
