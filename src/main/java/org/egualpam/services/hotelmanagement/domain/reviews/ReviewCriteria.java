package org.egualpam.services.hotelmanagement.domain.reviews;

import org.egualpam.services.hotelmanagement.domain.shared.Criteria;

public final class ReviewCriteria implements Criteria {

    private final HotelId hotelId;

    public ReviewCriteria(String hotelId) {
        this.hotelId = new HotelId(hotelId);
    }

    public HotelId getHotelId() {
        return hotelId;
    }
}
