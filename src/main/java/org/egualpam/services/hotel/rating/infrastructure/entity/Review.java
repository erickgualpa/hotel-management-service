package org.egualpam.services.hotel.rating.infrastructure.entity;

public class Review {

    private final String identifier;
    private final Integer rating;
    private final String comment;
    private final String hotelIdentifier;

    public Review(String identifier, Integer rating, String comment, String hotelIdentifier) {
        this.identifier = identifier;
        this.rating = rating;
        this.comment = comment;
        this.hotelIdentifier = hotelIdentifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getHotelIdentifier() {
        return hotelIdentifier;
    }
}
