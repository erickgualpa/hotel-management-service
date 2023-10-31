package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.Identifier;

public final class Review {

    private Identifier identifierVO;
    private Identifier hotelIdentifierVO;
    private Rating ratingVO;
    private Comment commentVO;

    // TODO: Remove this properties once unused
    private String identifier;
    private Integer rating;
    private String comment;

    public Review(Identifier identifier, Identifier hotelIdentifier, Rating rating, Comment comment) {
        this.identifierVO = identifier;
        this.hotelIdentifierVO = hotelIdentifier;
        this.ratingVO = rating;
        this.commentVO = comment;
    }

    // TODO: Remove this constructor once unused
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

    public Identifier getIdentifierVO() {
        return identifierVO;
    }

    public Identifier getHotelIdentifierVO() {
        return hotelIdentifierVO;
    }

    public Rating getRatingVO() {
        return ratingVO;
    }

    public Comment getCommentVO() {
        return commentVO;
    }
}