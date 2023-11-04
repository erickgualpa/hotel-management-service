package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.Comment;
import org.egualpam.services.hotel.rating.domain.shared.Identifier;
import org.egualpam.services.hotel.rating.domain.shared.Rating;

public final class Review {

    private final Identifier identifier;
    private final Identifier hotelIdentifier;
    private final Rating rating;
    private final Comment comment;

    public Review(Identifier identifier, Identifier hotelIdentifier, Rating rating, Comment comment) {
        this.identifier = identifier;
        this.hotelIdentifier = hotelIdentifier;
        this.rating = rating;
        this.comment = comment;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Identifier getHotelIdentifier() {
        return hotelIdentifier;
    }

    public Rating getRating() {
        return rating;
    }

    public Comment getComment() {
        return comment;
    }
}