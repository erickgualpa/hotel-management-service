package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.Criteria;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;

public final class ReviewCriteria implements Criteria {

    private final Identifier hotelId;

    public ReviewCriteria(Identifier hotelId) {
        this.hotelId = hotelId;
    }

    public Identifier getHotelId() {
        return hotelId;
    }
}
