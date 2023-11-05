package org.egualpam.services.hotel.rating.domain.shared;

public record Rating(Integer value) {
    public Rating {
        if (value < 1 || value > 5) {
            throw new InvalidRating();
        }
    }
}
