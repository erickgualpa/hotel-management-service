package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.Identifier;

public final class Review {

    private final Identifier identifierVO;
    private final Identifier hotelIdentifierVO;
    private final Rating ratingVO;
    private final Comment commentVO;

    public Review(Identifier identifier, Identifier hotelIdentifier, Rating rating, Comment comment) {
        this.identifierVO = identifier;
        this.hotelIdentifierVO = hotelIdentifier;
        this.ratingVO = rating;
        this.commentVO = comment;
    }

    public Identifier getIdentifier() {
        return identifierVO;
    }

    public Identifier getHotelIdentifier() {
        return hotelIdentifierVO;
    }

    public Rating getRating() {
        return ratingVO;
    }

    public Comment getComment() {
        return commentVO;
    }
}