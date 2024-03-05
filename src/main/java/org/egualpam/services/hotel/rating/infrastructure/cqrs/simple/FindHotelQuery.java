package org.egualpam.services.hotel.rating.infrastructure.cqrs.simple;

import org.egualpam.services.hotel.rating.application.shared.Query;

public final class FindHotelQuery implements Query {

    private final String hotelId;

    public FindHotelQuery(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelId() {
        return hotelId;
    }
}