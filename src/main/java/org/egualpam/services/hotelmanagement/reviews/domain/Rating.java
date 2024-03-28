package org.egualpam.services.hotelmanagement.reviews.domain;

import org.egualpam.services.hotelmanagement.reviews.domain.exception.InvalidRating;

public record Rating(Integer value) {
    public Rating {
        if (value < 1 || value > 5) {
            throw new InvalidRating();
        }
    }
}
