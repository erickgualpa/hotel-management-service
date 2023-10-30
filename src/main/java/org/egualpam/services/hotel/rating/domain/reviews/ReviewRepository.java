package org.egualpam.services.hotel.rating.domain.reviews;

import java.util.List;

public abstract class ReviewRepository {

    public abstract List<Review> findByHotelIdentifier(String hotelIdentifier);

    public abstract void save(Review review);

    protected final Review mapIntoEntity(String identifier, Integer rating, String comment) {
        return new Review(identifier, rating, comment);
    }
}
