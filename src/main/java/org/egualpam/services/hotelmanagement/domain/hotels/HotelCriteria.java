package org.egualpam.services.hotelmanagement.domain.hotels;

import org.egualpam.services.hotelmanagement.domain.shared.AggregateId;
import org.egualpam.services.hotelmanagement.domain.shared.Criteria;

import java.util.Optional;

public final class HotelCriteria implements Criteria {
    // TODO: Avoid using 'AggregateId' as 'Criteria' parameter
    private final Optional<AggregateId> hotelId;
    private final Optional<Location> location;
    private final PriceRange priceRange;

    public HotelCriteria(String hotelId) {
        this.hotelId = Optional.of(hotelId).map(AggregateId::new);
        this.location = Optional.empty();
        this.priceRange = new PriceRange(
                Optional.empty(),
                Optional.empty()
        );
    }

    public HotelCriteria(
            Optional<Location> location,
            PriceRange priceRange
    ) {
        this.location = location;
        this.priceRange = priceRange;
        this.hotelId = Optional.empty();
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

    public Optional<AggregateId> getHotelId() {
        return hotelId;
    }
}
