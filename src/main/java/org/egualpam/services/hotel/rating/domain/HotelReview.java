package org.egualpam.services.hotel.rating.domain;

import lombok.Getter;

@Getter
public final class HotelReview {

    private String identifier;
    private Integer rating;
    private String comment;

    public HotelReview() {}

    public HotelReview(String identifier, Integer rating, String comment) {
        this.identifier = identifier;
        this.rating = rating;
        this.comment = comment;
    }
}
