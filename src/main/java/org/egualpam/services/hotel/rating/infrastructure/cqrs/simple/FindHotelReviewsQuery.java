package org.egualpam.services.hotel.rating.infrastructure.cqrs.simple;

import org.egualpam.services.hotel.rating.infrastructure.cqrs.Query;

public final class FindHotelReviewsQuery implements Query {

    private final String hotelIdentifier;

    public FindHotelReviewsQuery(String hotelIdentifier) {
        this.hotelIdentifier = hotelIdentifier;
    }

    public String getHotelIdentifier() {
        return hotelIdentifier;
    }
}
