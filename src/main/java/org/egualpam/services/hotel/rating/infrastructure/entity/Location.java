package org.egualpam.services.hotel.rating.infrastructure.entity;

public class Location {
    private final String identifier;
    private final String name;

    public Location(String identifier, String name) {
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
