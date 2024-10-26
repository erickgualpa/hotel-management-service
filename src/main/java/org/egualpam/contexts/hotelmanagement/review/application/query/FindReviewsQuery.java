package org.egualpam.contexts.hotelmanagement.review.application.query;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;

public final class FindReviewsQuery implements Query {

    private final String hotelId;

    public FindReviewsQuery(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelId() {
        return hotelId;
    }
}
