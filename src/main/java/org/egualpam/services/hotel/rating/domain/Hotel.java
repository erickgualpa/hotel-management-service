package org.egualpam.services.hotel.rating.domain;

public final class Hotel {

    private String identifier;
    private String name;
    private String description;
    private HotelLocation location;
    private Integer totalPrice;
    private String imageURL;

    public Hotel() {}

    public Hotel(
            String identifier,
            String name,
            String description,
            HotelLocation location,
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

    public HotelLocation getLocation() {
        return location;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public String getImageURL() {
        return imageURL;
    }
}
