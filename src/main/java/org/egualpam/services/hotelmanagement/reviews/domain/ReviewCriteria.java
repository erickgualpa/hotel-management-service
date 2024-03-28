package org.egualpam.services.hotelmanagement.reviews.domain;

import org.egualpam.services.hotelmanagement.shared.domain.Criteria;

public final class ReviewCriteria implements Criteria {

    private final HotelId hotelId;

    public ReviewCriteria(String hotelId) {
        this.hotelId = new HotelId(hotelId);
    }

    public HotelId getHotelId() {
        return hotelId;
    }
}