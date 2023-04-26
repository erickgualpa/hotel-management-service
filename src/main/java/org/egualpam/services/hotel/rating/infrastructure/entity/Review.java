package org.egualpam.services.hotel.rating.infrastructure.entity;

public class Review {

    private String identifier;
    private Integer rating;
    private String comment;
    private String hotelIdentifier;

    public Review() {}

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
