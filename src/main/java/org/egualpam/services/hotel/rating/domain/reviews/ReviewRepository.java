package org.egualpam.services.hotel.rating.domain.reviews;

import org.egualpam.services.hotel.rating.domain.shared.Identifier;

import java.util.List;

public abstract class ReviewRepository {

    public abstract List<Review> findByHotelIdentifier(Identifier hotelIdentifier);

    public abstract void save(Review review);

    protected final Review mapIntoEntity(String identifier, String hotelIdentifier, Integer rating, String comment) {
        return new Review(
                new Identifier(identifier),
                new Identifier(hotelIdentifier),
                new Rating(rating),
                new Comment(comment)
        );
    }
}
