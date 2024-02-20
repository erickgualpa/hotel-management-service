package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.Criteria;

public final class ReviewCriteria implements Criteria {

    private final HotelId hotelId;

    public ReviewCriteria(HotelId hotelId) {
        this.hotelId = hotelId;
    }

    public HotelId getHotelId() {
        return hotelId;
    }
}
