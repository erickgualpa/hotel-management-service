package org.egualpam.services.hotelmanagement.hotels.domain;

import org.egualpam.services.hotelmanagement.hotels.domain.exception.PriceRangeValuesSwapped;

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
