package org.egualpam.services.hotel.rating.controller;

import java.time.LocalDate;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Query {

    @Getter private final String location;
    @Getter private final LocalDate checkIn;
    @Getter private final LocalDate checkOut;
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
