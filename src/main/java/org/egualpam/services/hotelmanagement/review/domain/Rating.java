package org.egualpam.services.hotelmanagement.review.domain;

import org.egualpam.services.hotelmanagement.review.domain.exceptions.InvalidRating;

public record Rating(Integer value) {
    public Rating {
        if (value < 1 || value > 5) {
            throw new InvalidRating();
        }
    }
}
