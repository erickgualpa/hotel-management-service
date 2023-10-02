package org.egualpam.services.hotel.rating.infrastructure.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public final class Query {

    @Getter
    private final String location;
    private final Query.PriceRange priceRange;

    public Integer getMinPrice() {
        return Optional.ofNullable(priceRange).map(PriceRange::getBegin).orElse(null);
    }

    public Integer getMaxPrice() {
        return Optional.ofNullable(priceRange).map(PriceRange::getEnd).orElse(null);
    }

    @Getter
    @RequiredArgsConstructor
    static class PriceRange {
        private final Integer begin;
        private final Integer end;
    }
}
