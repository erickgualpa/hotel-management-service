package org.egualpam.services.hotel.rating.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.egualpam.services.hotel.rating.application.ReviewRepository;
import org.egualpam.services.hotel.rating.domain.HotelReview;
import org.egualpam.services.hotel.rating.infrastructure.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class InMemoryReviewRepository implements ReviewRepository {

    private static final List<Review> inMemoryReviews = new ArrayList<>();

    static {
        inMemoryReviews.add(new Review("AMZ_REVIEW_1", 5, "Eloquent comment", "AMZ_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_2", 3, "Eloquent comment", "AMZ_HOTEL"));

        inMemoryReviews.add(new Review("AMZ_REVIEW_3", 1, "Eloquent comment", "AMZ_ROME_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_4", 3, "Eloquent comment", "AMZ_ROME_HOTEL"));

        inMemoryReviews.add(new Review("AMZ_REVIEW_5", 5, "Eloquent comment", "AMZ_BERLIN_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_6", 2, "Eloquent comment", "AMZ_BERLIN_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_7", 1, "Eloquent comment", "AMZ_BERLIN_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_8", 1, "Eloquent comment", "AMZ_BERLIN_HOTEL"));

        inMemoryReviews.add(new Review("AMZ_REVIEW_9", 1, "Eloquent comment", "NON_AMZ_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_11", 2, "Eloquent comment", "NON_AMZ_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_12", 1, "Eloquent comment", "NON_AMZ_HOTEL"));

        inMemoryReviews.add(new Review("AMZ_REVIEW_13", 4, "Eloquent comment", "MEDIUM_AMZ_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_14", 4, "Eloquent comment", "MEDIUM_AMZ_HOTEL"));
        inMemoryReviews.add(new Review("AMZ_REVIEW_15", 3, "Eloquent comment", "MEDIUM_AMZ_HOTEL"));
    }

    @Override
    public List<HotelReview> findReviewsMatchingHotelIdentifier(String hotelIdentifier) {
        return inMemoryReviews.stream()
                .filter(r -> r.getHotelIdentifier().equals(hotelIdentifier))
                .map(this::mapToHotelReview)
                .collect(Collectors.toList());
    }

    private HotelReview mapToHotelReview(Review r) {
        return new HotelReview(r.getIdentifier(), r.getRating(), r.getComment());
    }
}