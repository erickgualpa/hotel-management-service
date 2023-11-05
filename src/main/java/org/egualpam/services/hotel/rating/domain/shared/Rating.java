package org.egualpam.services.hotel.rating.domain.shared;

import org.egualpam.services.hotel.rating.application.reviews.InvalidRating;

public record Rating(Integer value) {
    public Rating {
        if (value < 1 || value > 5) {
            throw new InvalidRating();
        }
    }
}
