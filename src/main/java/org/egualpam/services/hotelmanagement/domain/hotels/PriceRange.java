package org.egualpam.services.hotelmanagement.domain.hotels;

import org.egualpam.services.hotelmanagement.domain.hotels.exception.PriceRangeValuesSwapped;

import java.util.Optional;

public record PriceRange(Optional<Price> minPrice, Optional<Price> maxPrice) {
    public PriceRange {
        maxPrice.ifPresent(
                max -> minPrice.ifPresent(
                        min -> {
                            if (min.value() > max.value()) {
                                throw new PriceRangeValuesSwapped();
                            }
                        }
                )
        );
    }
}
