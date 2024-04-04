package org.egualpam.services.hotelmanagement.reviews.domain;

import org.egualpam.services.hotelmanagement.shared.domain.Criteria;

import java.util.Optional;

import static java.util.Objects.nonNull;

public final class ReviewCriteria implements Criteria {

    private final HotelId hotelId;

    public ReviewCriteria(String hotelId) {
        this.hotelId = nonNull(hotelId) ? new HotelId(hotelId) : null;
    }

    public Optional<HotelId> getHotelId() {
        return Optional.ofNullable(hotelId);
    }
}
