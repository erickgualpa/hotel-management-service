package org.egualpam.services.hotel.rating.infrastructure.controller;

import org.egualpam.services.hotel.rating.infrastructure.cqrs.Query;

import java.util.Optional;

public final class FindHotelsQuery implements Query {
    private final Optional<String> locationFilter;
    private final Optional<Integer> minPriceFilter;
    private final Optional<Integer> maxPriceFilter;

    public FindHotelsQuery(
            Optional<String> locationFilter,
            Optional<Integer> minPriceFilter,
            Optional<Integer> maxPriceFilter) {
        this.locationFilter = locationFilter;
        this.minPriceFilter = minPriceFilter;
        this.maxPriceFilter = maxPriceFilter;
    }

    public Optional<String> getLocationFilter() {
        return locationFilter;
    }

    public Optional<Integer> getMinPriceFilter() {
        return minPriceFilter;
    }

    public Optional<Integer> getMaxPriceFilter() {
        return maxPriceFilter;
    }
}
