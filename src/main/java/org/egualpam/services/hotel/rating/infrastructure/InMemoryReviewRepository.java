package org.egualpam.services.hotel.rating.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.application.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.Review;
import org.springframework.stereotype.Component;

@Component
public class InMemoryReviewRepository implements ReviewRepository {

    private static final List<Review> inMemoryReviews = new ArrayList<>();

    static {
        inMemoryReviews.add(new Review("AMZ_REVIEW", 5, "Eloquent comment", "AMZ_HOTEL"));
    }

    @Override
    public List<Review> findReviewsMatchingHotelIdentifier(String hotelIdentifier) {
        return inMemoryReviews.stream()
                .filter(r -> r.getHotelIdentifier().equals(hotelIdentifier))
                .collect(Collectors.toList());
    }
}