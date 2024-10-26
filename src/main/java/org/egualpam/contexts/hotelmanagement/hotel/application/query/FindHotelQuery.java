package org.egualpam.contexts.hotelmanagement.hotel.application.query;

import org.egualpam.contexts.hotelmanagement.shared.application.query.Query;

public final class FindHotelQuery implements Query {

    private final String hotelId;

    public FindHotelQuery(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelId() {
        return hotelId;
    }
}