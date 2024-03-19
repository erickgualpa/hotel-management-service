package org.egualpam.services.hotelmanagement.infrastructure.cqrs.simple;

import org.egualpam.services.hotelmanagement.application.shared.Query;

public final class FindHotelQuery implements Query {

    private final String hotelId;

    public FindHotelQuery(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelId() {
        return hotelId;
    }
}