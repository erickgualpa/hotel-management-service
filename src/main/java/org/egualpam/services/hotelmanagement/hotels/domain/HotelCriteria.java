package org.egualpam.services.hotelmanagement.hotels.domain;

import org.egualpam.services.hotelmanagement.hotels.domain.exception.PriceRangeValuesSwapped;
import org.egualpam.services.hotelmanagement.shared.domain.Criteria;
import org.egualpam.services.hotelmanagement.shared.domain.UniqueId;

import java.util.Optional;

import static java.util.Objects.nonNull;

public final class HotelCriteria implements Criteria {

    private final UniqueId hotelId;
    private final Location location;
    private final Price minPrice;
    private final Price maxPrice;

    public HotelCriteria(String hotelId) {
        this.hotelId = nonNull(hotelId) ? new UniqueId(hotelId) : null;
        this.location = null;
        this.minPrice = null;
        this.maxPrice = null;
    }

    public HotelCriteria(
            String location,
            Integer minPrice,
            Integer maxPrice
    ) {
        this.hotelId = null;
        this.location = nonNull(location) ? new Location(location) : null;

        if (nonNull(minPrice) && nonNull(maxPrice) && minPrice > maxPrice) {
            throw new PriceRangeValuesSwapped();
        }
        this.minPrice = nonNull(minPrice) ? new Price(minPrice) : null;
        this.maxPrice = nonNull(maxPrice) ? new Price(maxPrice) : null;
    }

    public Optional<UniqueId> getHotelId() {
        return Optional.ofNullable(hotelId);
    }

    public Optional<Location> getLocation() {
        return Optional.ofNullable(location);
    }

    public Optional<Price> getMinPrice() {
        return Optional.ofNullable(minPrice);
    }

    public Optional<Price> getMaxPrice() {
        return Optional.ofNullable(maxPrice);
    }
}
