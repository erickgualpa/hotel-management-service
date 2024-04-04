package org.egualpam.services.hotelmanagement.reviews.application.query;

import org.egualpam.services.hotelmanagement.shared.application.query.Query;

public final class FindReviewsQuery implements Query {

    private final String hotelIdentifier;

    public FindReviewsQuery(String hotelIdentifier) {
        this.hotelIdentifier = hotelIdentifier;
    }

    public String getHotelIdentifier() {
        return hotelIdentifier;
    }
}
