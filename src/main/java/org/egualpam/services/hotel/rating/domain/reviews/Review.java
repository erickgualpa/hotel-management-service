package org.egualpam.services.hotel.rating.domain.reviews;

public final class Review {

    private final String identifier;
    private final Integer rating;
    private final String comment;

    public Review(String identifier, Integer rating, String comment) {
        this.identifier = identifier;
        this.rating = rating;
        this.comment = comment;
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
}
