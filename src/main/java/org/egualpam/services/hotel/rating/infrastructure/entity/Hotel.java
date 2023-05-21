package org.egualpam.services.hotel.rating.infrastructure.entity;

public class Hotel {

    private final String identifier;
    private final String name;
    private final String description;
    private final Location location;

    // Assuming this represents total price per night
    private final Integer totalPrice;

    private final String imageURL;

    public Hotel(
            String identifier,
            String name,
            String description,
            Location location,
            Integer totalPrice,
            String imageURL) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.location = location;
        this.totalPrice = totalPrice;
        this.imageURL = imageURL;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public String getImageURL() {
        return imageURL;
    }
}
