package org.egualpam.services.hotelmanagement.shared.infrastructure.cqrs.query.simple;

import org.egualpam.services.hotelmanagement.shared.application.query.Query;

public final class FindHotelQuery implements Query {

    private final String hotelId;

    public FindHotelQuery(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelId() {
        return hotelId;
    }
}