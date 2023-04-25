package org.egualpam.services.hotel.rating.domain;

public class HotelLocation {
    private final String identifier;
    private final String name;

    public HotelLocation(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }
}
