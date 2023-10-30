package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.Identifier;

public final class Review {

    private Identifier identifierVO;
    private Rating ratingVO;
    private Comment commentVO;

    // TODO: Remove this properties once unused
    private String identifier;
    private Integer rating;
    private String comment;

    // TODO: Remove this constructor once unused
    public Review(Identifier identifier, Rating rating, Comment comment) {
        this.identifierVO = identifier;
        this.ratingVO = rating;
        this.commentVO = comment;
    }

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