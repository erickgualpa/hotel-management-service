package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.services.hotelmanagement.application.shared.Query;

public final class FindHotelReviewsQuery implements Query {

    private final String hotelIdentifier;

    public FindHotelReviewsQuery(String hotelIdentifier) {
        this.hotelIdentifier = hotelIdentifier;
    }

    public String getHotelIdentifier() {
        return hotelIdentifier;
    }
}
