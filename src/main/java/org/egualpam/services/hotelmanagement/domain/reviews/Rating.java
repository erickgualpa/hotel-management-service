package org.egualpam.services.hotelmanagement.domain.reviews;

import org.egualpam.services.hotelmanagement.domain.reviews.exception.InvalidRating;

public record Rating(Integer value) {
    public Rating {
        if (value < 1 || value > 5) {
            throw new InvalidRating();
        }
    }
}
